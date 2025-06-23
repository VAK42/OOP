package com.vak.oop.controller;

import com.vak.oop.model.Report;
import com.vak.oop.service.ReportService;
import com.vak.oop.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/reports")
public class ReportController {
  private final ReportService service;
  private final UserService userService;

  public ReportController(ReportService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  @GetMapping
  public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "rpName") String field, @RequestParam(defaultValue = "asc") String direction, Model model) {
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(field).ascending() : Sort.by(field).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Report> reportPage = keyword.isBlank() ? service.findAll(pageable) : service.search(keyword, pageable);
    model.addAttribute("reports", reportPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", reportPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("field", field);
    model.addAttribute("direction", direction);
    model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
    model.addAttribute("view", "report/list");
    return "index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("report", new Report());
    model.addAttribute("view", "report/form");
    return "index";
  }

  @PostMapping
  public String save(@ModelAttribute Report report) {
    userService.findByRole("admin", Pageable.unpaged()).stream().findFirst().ifPresent(report::setUser);
    service.save(report);
    return "redirect:/reports";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable UUID id, Model model) {
    model.addAttribute("report", service.findById(id).orElseThrow());
    model.addAttribute("view", "report/form");
    return "index";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable UUID id) {
    service.deleteById(id);
    return "redirect:/reports";
  }
}