package com.example.personalfinancemanager.controller;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllCategories(Model model,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        if (page < 0) page = 0;

        Page<Category> categoryPage = categoryService.getAllCategoriesForPage(page);
        if (categoryPage.getTotalPages() < page) return "redirect:/categories";

        model.addAttribute("categories", categoryService.getAllCategoriesForPage(page));
        model.addAttribute("page", page);

        return "category/categories";
    }

    @GetMapping(path = "/new")
    public String categoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/new-update-category";
    }

    @PostMapping(path = "/new")
    public String categorySubmit(@Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "category/new-update-category";
        }

        if (!categoryService.createCategory(category)) {
            model.addAttribute(
                    "nameAlreadyExistError",
                    "???????? ?????????????????? ?????? ??????????.");
            return "category/new-update-category";
        }

        categoryService.createCategory(category);

        redirectAttributes.addFlashAttribute(
                "successCategorySubmitMessage",
                "?????????????????? \"" + category.getName() + "\" ????????????.");
        return "redirect:/categories";
    }

    @GetMapping(path = "{id}/update")
    public String updateCategoryForm(@PathVariable("id") Long id,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        Optional<Category> category = categoryService.getCategoryById(id);

        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            model.addAttribute("updateCategory", true);
        } else {
            redirectAttributes.addFlashAttribute(
                    "failureCategoryMessage",
                    "?????????????? ??????????????, ?????????????????? ??????????????.");
            return "redirect:/categories";
        }

        return "category/new-update-category";
    }

    @PutMapping(path = "{id}/update")
    public String updateCategory(@PathVariable("id") Long id,
                                 Model model,
                                 @Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            category.setId(id);
            model.addAttribute("category", category);
            model.addAttribute("updateCategory", true);
            return "category/new-update-category";
        }

        switch (categoryService.updateCategoryById(id, category)) {
            case 1 -> {
                redirectAttributes.addFlashAttribute(
                        "successCategoryUpdateMessage",
                        "?????????????????? \"" + category.getName() + "\" ????????????????.");
            }
            case 2 -> {
                category.setId(id);
                model.addAttribute("category", category);
                model.addAttribute("updateCategory", true);
                model.addAttribute(
                        "nameAlreadyExistError",
                        "???????? ?????????????????? ?????? ??????????.");
                return "category/new-update-category";
            }
            default -> {
                redirectAttributes.addFlashAttribute(
                        "failureCategoryMessage",
                        "?????????????? ??????????????, ?????????????????? ??????????????.");
            }
        }

        return "redirect:/categories";
    }


    @DeleteMapping(path = "{id}/delete")
    public String deleteCategory(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {
        Optional<Category> category = categoryService.getCategoryById(id);

        if (category.isPresent()) {
            categoryService.deleteCategoryById(id);
            redirectAttributes.addFlashAttribute(
                    "successCategoryDeleteMessage",
                    "?????????????????? \"" + category.get().getName() + "\" ????????????????.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "failureCategoryMessage",
                    "?????????????? ??????????????, ?????????????????? ??????????????.");
        }

        return "redirect:/categories";
    }
}
