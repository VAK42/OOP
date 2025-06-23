package com.vak.oop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RequestControllerAdvice {
  @ModelAttribute
  public void addRequestToModel(HttpServletRequest request, Model model) {
    model.addAttribute("request", request);
  }
}