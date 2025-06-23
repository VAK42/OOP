package com.vak.oop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
  @PersistenceContext
  private EntityManager entityManager;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping("/")
  public String index(Model model) throws JsonProcessingException {
    Map<String, Object> dashboardData = getDashboardData();
    String monthlyRevenueJson = objectMapper.writeValueAsString(dashboardData.get("monthlyRevenue"));
    String topProductsJson = objectMapper.writeValueAsString(dashboardData.get("topProducts"));
    model.addAttribute("view", "dashboard/index");
    model.addAttribute("dashboardData", dashboardData);
    model.addAttribute("monthlyRevenueJson", monthlyRevenueJson);
    model.addAttribute("topProductsJson", topProductsJson);
    return "index";
  }

  private Map<String, Object> getDashboardData() {
    Map<String, Object> dashboardData = new HashMap<>();
    LocalDate now = LocalDate.now();
    LocalDate startOfMonth = now.withDayOfMonth(1);
    LocalDate startOfYear = now.withDayOfYear(1);
    LocalDate startOfNextMonth = startOfMonth.plusMonths(1);
    LocalDate startOfNextYear = startOfYear.plusYears(1);
    LocalDateTime monthStart = startOfMonth.atStartOfDay();
    LocalDateTime monthEnd = startOfNextMonth.atStartOfDay();
    LocalDateTime yearStart = startOfYear.atStartOfDay();
    LocalDateTime yearEnd = startOfNextYear.atStartOfDay();
    BigDecimal revenueThisMonth = calculateRevenue(monthStart, monthEnd);
    BigDecimal revenueThisYear = calculateRevenue(yearStart, yearEnd);
    String bestSellingProductMonth = getBestSellingProduct(monthStart, monthEnd);
    String bestSellingProductYear = getBestSellingProduct(yearStart, yearEnd);
    List<Map<String, Object>> monthlyRevenue = getMonthlyRevenueData();
    List<Map<String, Object>> topProducts = getTopProductsThisMonth(monthStart, monthEnd);
    dashboardData.put("revenueThisMonth", revenueThisMonth);
    dashboardData.put("revenueThisYear", revenueThisYear);
    dashboardData.put("bestSellingProductMonth", bestSellingProductMonth);
    dashboardData.put("bestSellingProductYear", bestSellingProductYear);
    dashboardData.put("monthlyRevenue", monthlyRevenue);
    dashboardData.put("topProducts", topProducts);
    return dashboardData;
  }

  private BigDecimal calculateRevenue(LocalDateTime start, LocalDateTime end) {
    String jpql = "SELECT SUM(e.pdTotalPrice) FROM Export e WHERE e.date >= :start AND e.date < :end";
    TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
    query.setParameter("start", start);
    query.setParameter("end", end);
    BigDecimal result = query.getSingleResult();
    return result != null ? result : BigDecimal.ZERO;
  }

  private String getBestSellingProduct(LocalDateTime start, LocalDateTime end) {
    String jpql = "SELECT e.product.pdName, SUM(e.pdQuantity) as totalQuantity " + "FROM Export e WHERE e.date >= :start AND e.date < :end " + "GROUP BY e.product.pdName ORDER BY totalQuantity DESC";
    TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
    query.setParameter("start", start);
    query.setParameter("end", end);
    query.setMaxResults(1);
    List<Object[]> results = query.getResultList();
    if (!results.isEmpty()) {
      return (String) results.getFirst()[0];
    }
    return "No Sales";
  }

  private List<Map<String, Object>> getMonthlyRevenueData() {
    List<Map<String, Object>> monthlyData = new ArrayList<>();
    LocalDate now = LocalDate.now();
    for (int i = 11; i >= 0; i--) {
      LocalDate monthDate = now.minusMonths(i);
      LocalDate startOfMonth = monthDate.withDayOfMonth(1);
      LocalDate endOfMonth = startOfMonth.plusMonths(1);
      LocalDateTime monthStart = startOfMonth.atStartOfDay();
      LocalDateTime monthEnd = endOfMonth.atStartOfDay();
      BigDecimal revenue = calculateRevenue(monthStart, monthEnd);
      Map<String, Object> monthData = new HashMap<>();
      monthData.put("month", monthDate.format(DateTimeFormatter.ofPattern("MMM yyyy")));
      monthData.put("revenue", revenue);
      monthlyData.add(monthData);
    }
    return monthlyData;
  }

  private List<Map<String, Object>> getTopProductsThisMonth(LocalDateTime start, LocalDateTime end) {
    String jpql = "SELECT e.product.pdName, SUM(e.pdQuantity) as totalQuantity " + "FROM Export e WHERE e.date >= :start AND e.date < :end " + "GROUP BY e.product.pdName ORDER BY totalQuantity DESC";
    TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
    query.setParameter("start", start);
    query.setParameter("end", end);
    query.setMaxResults(10);
    List<Object[]> results = query.getResultList();
    return results.stream().map(result -> {
      Map<String, Object> productData = new HashMap<>();
      productData.put("name", result[0]);
      productData.put("quantity", ((Number) result[1]).intValue());
      return productData;
    }).collect(Collectors.toList());
  }
}