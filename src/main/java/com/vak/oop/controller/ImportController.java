package com.vak.oop.controller;

import com.vak.oop.model.Import;
import com.vak.oop.service.ImportService;
import com.vak.oop.service.ProductService;
import com.vak.oop.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/imports")
public class ImportController {
  private final ImportService service;
  private final ProductService productService;
  private final UserService userService;

  public ImportController(ImportService service, ProductService productService, UserService userService) {
    this.service = service;
    this.productService = productService;
    this.userService = userService;
  }

  @GetMapping
  public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "date") String field, @RequestParam(defaultValue = "desc") String direction, Model model) {
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(field).ascending() : Sort.by(field).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Import> importPage = keyword.isBlank() ? service.findAll(pageable) : service.search(keyword, pageable);
    model.addAttribute("imports", importPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", importPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("field", field);
    model.addAttribute("direction", direction);
    model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
    model.addAttribute("view", "import/list");
    return "index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("import", new Import());
    model.addAttribute("products", productService.findAll(Pageable.unpaged()).getContent());
    model.addAttribute("view", "import/form");
    return "index";
  }

  @PostMapping
  public String save(@ModelAttribute Import imp) {
    userService.findByRole("admin", Pageable.unpaged()).stream().findFirst().ifPresent(imp::setUser);
    service.save(imp);
    return "redirect:/imports";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable UUID id, Model model) {
    model.addAttribute("import", service.findById(id).orElseThrow());
    model.addAttribute("products", productService.findAll(Pageable.unpaged()).getContent());
    model.addAttribute("view", "import/form");
    return "index";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable UUID id) {
    service.deleteById(id);
    return "redirect:/imports";
  }
}