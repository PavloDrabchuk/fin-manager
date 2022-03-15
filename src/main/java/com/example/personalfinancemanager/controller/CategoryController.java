package com.example.personalfinancemanager.controller;

import com.example.personalfinancemanager.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path="/")
    public String getAllCategories(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        System.out.println("categories");
        return "categories";
    }


}
