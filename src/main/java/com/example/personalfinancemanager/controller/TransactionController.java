package com.example.personalfinancemanager.controller;

import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Transaction;
import com.example.personalfinancemanager.service.impl.CategoryServiceImpl;
import com.example.personalfinancemanager.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
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
                                     @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("transactions", transactionService.getAllTransactionsForPage(page));
        model.addAttribute("page", page);

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
    public String transactionSubmit(@Valid @ModelAttribute Transaction transaction,
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

        redirectAttributes.addFlashAttribute("successTransactionSubmitMessage", "Транзакцію додано");
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
            redirectAttributes.addFlashAttribute("failureTransactionMessage", "Сталась помилка, спробуйте пізніше");
            return "redirect:/transactions";
        }

        return "transaction/new-update-transaction";
    }

    @PutMapping(path = "{id}/update")
    public String updateTransaction(@PathVariable("id") Long id,
                                    Model model,
                                    @Valid @ModelAttribute Transaction transaction,
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
            redirectAttributes.addFlashAttribute("successTransactionUpdateMessage", "Транзакцію оновлено");
        } else {
            redirectAttributes.addFlashAttribute("failureTransactionMessage", "Сталась помилка, спробуйте пізніше");
        }
        return "redirect:/transactions";
    }

    @DeleteMapping(path = "{id}/delete")
    public String deleteTransaction(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);

        if (transaction.isPresent()) {
            transactionService.deleteTransactionById(id);
            redirectAttributes.addFlashAttribute("successTransactionDeleteMessage", "Транзакцію видалено");
        } else {
            redirectAttributes.addFlashAttribute("failureTransactionMessage", "Сталась помилка, спробуйте пізніше");
        }

        return "redirect:/transactions";
    }
}
