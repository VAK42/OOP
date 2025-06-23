package com.vak.oop.controller;

import com.vak.oop.model.Category;
import com.vak.oop.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/categories")
public class CategoryController {
  private final CategoryService service;

  public CategoryController(CategoryService service) {
    this.service = service;
  }

  @GetMapping
  public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "name") String field, @RequestParam(defaultValue = "asc") String direction, Model model) {
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(field).ascending() : Sort.by(field).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Category> categoryPage = keyword.isBlank() ? service.findAll(pageable) : service.search(keyword, pageable);
    model.addAttribute("categories", categoryPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", categoryPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("field", field);
    model.addAttribute("direction", direction);
    model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
    model.addAttribute("view", "category/list");
    return "index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("category", new Category());
    model.addAttribute("view", "category/form");
    return "index";
  }

  @PostMapping
  public String save(@ModelAttribute Category category, Model model) {
    boolean isNew = (category.getCategoryId() == null);
    boolean nameExisted = service.existsByName(category.getName());
    if (isNew && nameExisted) {
      model.addAttribute("error", "Already Existed!");
      model.addAttribute("category", category);
      model.addAttribute("view", "category/form");
      return "index";
    }
    if (!isNew) {
      Category existing = service.findById(category.getCategoryId()).orElseThrow();
      if (!existing.getName().equalsIgnoreCase(category.getName()) && nameExisted) {
        model.addAttribute("error", "Already Existed!");
        model.addAttribute("category", category);
        model.addAttribute("view", "category/form");
        return "index";
      }
    }
    service.save(category);
    return "redirect:/categories";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable UUID id, Model model) {
    model.addAttribute("category", service.findById(id).orElseThrow());
    model.addAttribute("view", "category/form");
    return "index";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable UUID id) {
    service.deleteById(id);
    return "redirect:/categories";
  }
}