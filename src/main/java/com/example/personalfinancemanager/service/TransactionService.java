package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.dto.ReportByCategoriesDTO;
import com.example.personalfinancemanager.dto.ReportCostDynamicsForCategoryDTO;
import com.example.personalfinancemanager.dto.ReportDayByDayDTO;
import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    void createTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();

    Page<Transaction> getAllTransactionsForPage(Integer pageNo);

    Optional<Transaction> getTransactionById(Long id);

    boolean updateTransactionById(long id, Transaction newTransaction);

    void deleteTransactionById(Long id);

    void deleteAllTransactions();

    List<ReportDayByDayDTO> generateDayByDayReport(OperationType operationType, String from, String to) throws ParseException;

    List<ReportByCategoriesDTO> generateReportByCategories(OperationType operationType, String from, String to) throws ParseException;

    Double getTotalSumBetweenDays(OperationType operationType, String from, String to) throws ParseException;

    List<Integer> getYearsBetweenTwoDates(Long categoryId, OperationType operationType, String dateFrom, String dateTo) throws ParseException;

    List<ReportCostDynamicsForCategoryDTO> getTotalSumByMonthForCategory(Long categoryId, OperationType operationType, Integer year);

    List<ReportCostDynamicsForCategoryDTO> generateCostDynamicsReportForCategory(Long categoryId, OperationType operationType, String dateFrom, String dateTo) throws ParseException;

    Page<Transaction> getAllTransactionForPageByCategory(Integer pageNo, Category category);
}
