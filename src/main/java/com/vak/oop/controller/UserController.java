package com.vak.oop.controller;

import com.vak.oop.model.User;
import com.vak.oop.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping
  public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "username") String field, @RequestParam(defaultValue = "asc") String direction, Model model) {
    Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(field).ascending() : Sort.by(field).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<User> userPage;
    if (!keyword.isBlank()) {
      userPage = service.searchByUsernameAndRole("user", keyword, pageable);
    } else {
      userPage = service.findByRole("user", pageable);
    }
    model.addAttribute("users", userPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", userPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    model.addAttribute("field", field);
    model.addAttribute("direction", direction);
    model.addAttribute("reverseSortDir", direction.equals("asc") ? "desc" : "asc");
    model.addAttribute("view", "user/list");
    return "index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("view", "user/form");
    return "index";
  }

  @PostMapping
  public String save(@ModelAttribute("user") User user, Model model) {
    boolean isNew = (user.getUserId() == null);
    boolean usernameExisted = service.existsByUsername(user.getUsername());
    if (isNew && usernameExisted) {
      model.addAttribute("error", "Already Existed!");
      model.addAttribute("user", user);
      model.addAttribute("view", "user/form");
      return "index";
    }
    if (!isNew) {
      User existing = service.findById(user.getUserId()).orElseThrow();
      if (!existing.getUsername().equalsIgnoreCase(user.getUsername()) && usernameExisted) {
        model.addAttribute("error", "Already Existed");
        model.addAttribute("user", user);
        model.addAttribute("view", "user/form");
        return "index";
      }
    }
    service.save(user);
    return "redirect:/users";
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable UUID id, Model model) {
    model.addAttribute("user", service.findById(id).orElseThrow());
    model.addAttribute("view", "user/form");
    return "index";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable UUID id) {
    service.deleteById(id);
    return "redirect:/users";
  }
}