package com.vak.oop.controller;

import com.vak.oop.model.Product;
import com.vak.oop.service.ProductService;
import com.vak.oop.service.CategoryService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;
  private final CategoryService categoryService;

  public ProductController(ProductService productService, CategoryService categoryService) {
    this.productService = productService;
    this.categoryService = categoryService;
  }

  @GetMapping
  public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "pdName") String field, @RequestParam(defaultValue = "asc") String direction, Model model) {
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(field).ascending() : Sort.by(field).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Product> productPage = keyword.isBlank() ? productService.findAll(pageable) : productService.search(keyword, pageable);
    model.addAttribute("products", productPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", productPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("field", field);
    model.addAttribute("direction", direction);
    model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
    model.addAttribute("view", "product/list");
    return "index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("product", new Product());
    model.addAttribute("categories", categoryService.findAll(Pageable.unpaged()).getContent());
    model.addAttribute("view", "product/form");
    return "index";
  }

  @PostMapping
  public String save(@ModelAttribute Product product, Model model) {
    boolean isNew = (product.getPdId() == null);
    boolean nameExists = productService.existsByPdName(product.getPdName());
    if (isNew && nameExists) {
      model.addAttribute("error", "Product name already exists.");
      model.addAttribute("product", product);
      model.addAttribute("categories", categoryService.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
      model.addAttribute("view", "product/form");
      return "index";
    }
    if (!isNew) {
      Product existing = productService.findById(product.getPdId()).orElseThrow();
      if (!existing.getPdName().equalsIgnoreCase(product.getPdName()) && nameExists) {
        model.addAttribute("error", "Product name already exists.");
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll(PageRequest.of(0, Integer.MAX_VALUE)).getContent());
        model.addAttribute("view", "product/form");
        return "index";
      }
    }
    productService.save(product);
    return "redirect:/products";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable UUID id, Model model) {
    model.addAttribute("product", productService.findById(id).orElseThrow());
    model.addAttribute("categories", categoryService.findAll(Pageable.unpaged()).getContent());
    model.addAttribute("view", "product/form");
    return "index";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable UUID id) {
    productService.deleteById(id);
    return "redirect:/products";
  }
}