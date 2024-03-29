package com.example.personalfinancemanager.controller;

import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import com.example.personalfinancemanager.service.impl.CategoryServiceImpl;
import com.example.personalfinancemanager.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionServiceImpl transactionService;
    private final CategoryServiceImpl categoryService;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService,
                                 CategoryServiceImpl categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllTransactions(Model model,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "id") String sortBy,
                                     @RequestParam(defaultValue = "-1") String categoryId) {
        if (page < 0) page = 0;

        Optional<Category> category = categoryService.getCategoryById(Long.parseLong(categoryId));

        Page<Transaction> transactionPage;

        transactionPage = category.isPresent()
                ? transactionService.getAllTransactionForPageByCategory(page, category.get())
                : transactionService.getAllTransactionsForPage(page);

        if (transactionPage.getTotalPages() < page) return "redirect:/transactions";

        model.addAttribute("transactions", transactionPage);
        model.addAttribute("page", page);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("categoryId", categoryId);

        return "transaction/transactions";
    }

    @GetMapping(path = "/new")
    public String transactionForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("operationTypes", Arrays.asList(OperationType.values()));
        return "transaction/new-update-transaction";
    }

    @PostMapping(path = "/new")
    public String transactionSubmit(@Valid @ModelAttribute("transaction") Transaction transaction,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("transaction", transaction);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("operationTypes", Arrays.asList(OperationType.values()));

            return "transaction/new-update-transaction";
        }

        transactionService.createTransaction(transaction);

        redirectAttributes.addFlashAttribute(
                "successTransactionSubmitMessage",
                "Транзакцію додано.");
        return "redirect:/transactions";
    }

    @GetMapping(path = "{id}/update")
    public String updateTransactionForm(@PathVariable("id") Long id,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);

        if (transaction.isPresent()) {
            model.addAttribute("transaction", transaction.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("operationTypes", Arrays.asList(OperationType.values()));

            model.addAttribute("updateTransaction", true);
        } else {
            redirectAttributes.addFlashAttribute(
                    "failureTransactionMessage",
                    "Сталась помилка, спробуйте пізніше.");
            return "redirect:/transactions";
        }

        return "transaction/new-update-transaction";
    }

    @PutMapping(path = "{id}/update")
    public String updateTransaction(@PathVariable("id") Long id,
                                    Model model,
                                    @Valid @ModelAttribute("transaction") Transaction transaction,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            transaction.setId(id);
            model.addAttribute("transaction", transaction);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("operationTypes", Arrays.asList(OperationType.values()));

            model.addAttribute("updateTransaction", true);
            return "transaction/new-update-transaction";
        }

        if (transactionService.updateTransactionById(id, transaction)) {
            redirectAttributes.addFlashAttribute(
                    "successTransactionUpdateMessage",
                    "Транзакцію оновлено.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "failureTransactionMessage",
                    "Сталась помилка, спробуйте пізніше.");
        }
        return "redirect:/transactions";
    }

    @DeleteMapping(path = "{id}/delete")
    public String deleteTransaction(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);

        if (transaction.isPresent()) {
            transactionService.deleteTransactionById(id);
            redirectAttributes.addFlashAttribute(
                    "successTransactionDeleteMessage",
                    "Транзакцію видалено.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "failureTransactionMessage",
                    "Сталась помилка, спробуйте пізніше.");
        }

        return "redirect:/transactions";
    }

    @GetMapping(path = "report")
    public String reportGeneratorForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("operationTypes", Arrays.asList(OperationType.values()));

        return "report/report-generator-form";
    }

    @PostMapping(path = "report/view")
    public String generateReport(Model model,
                                 @RequestParam(defaultValue = "ByDay") String reportType,
                                 @RequestParam(defaultValue = "2020-01-01") String dateFrom,
                                 @RequestParam(defaultValue = "2025-01-01") String dateTo,
                                 @RequestParam("operationType") OperationType operationType,
                                 @RequestParam(name = "category", defaultValue = "") String categoryId,
                                 RedirectAttributes redirectAttributes) throws ParseException {

        model.addAttribute("reportType", reportType);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("operationType", operationType);

        switch (reportType) {
            case "ByDay" -> {
                model.addAttribute("reportDayByDay", transactionService.generateDayByDayReport(operationType, dateFrom, dateTo));
            }
            case "ByCategories" -> {
                model.addAttribute("reportByCategories", transactionService.generateReportByCategories(operationType, dateFrom, dateTo));
                model.addAttribute("totalSum", transactionService.getTotalSumBetweenDays(operationType, dateFrom, dateTo));
            }
            case "CostDynamics" -> {
                if (categoryId.equals("")) {
                    redirectAttributes.addFlashAttribute(
                            "failureReportMessage",
                            "Оберіть категорію для формування звіту.");
                    return "redirect:/transactions/report";
                }

                Long id = Long.parseLong(categoryId);
                Optional<Category> category = categoryService.getCategoryById(id);

                if (category.isPresent()) {
                    model.addAttribute("reportCostDynamicsForCategory", transactionService.generateCostDynamicsReportForCategory(id, operationType, dateFrom, dateTo));
                    model.addAttribute("category", category.get());
                } else {
                    redirectAttributes.addFlashAttribute(
                            "failureReportMessage",
                            "Сталась помилка, спробуйте пізніше.");
                    return "redirect:/transactions/report";
                }
            }
            default -> {
                redirectAttributes.addFlashAttribute(
                        "failureReportMessage",
                        "Сталась помилка, спробуйте пізніше.");
                return "redirect:/transactions/report";
            }
        }
        return "report/report";
    }

    @GetMapping("report/view")
    public String generatedReport(Model model) {
        return "redirect:/transactions/report";
    }

}
