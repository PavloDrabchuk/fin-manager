package com.example.personalfinancemanager.service.impl;

import com.example.personalfinancemanager.dto.ReportByCategoriesDTO;
import com.example.personalfinancemanager.dto.ReportCostDynamicsForCategoryDTO;
import com.example.personalfinancemanager.dto.ReportDayByDayDTO;
import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import com.example.personalfinancemanager.repository.TransactionRepository;
import com.example.personalfinancemanager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    private final Integer PAGE_SIZE_PAGINATION = 7;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    @Override
    public Page<Transaction> getAllTransactionsForPage(Integer pageNo) {
        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE_PAGINATION);

        return transactionRepository.findAll(paging);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public boolean updateTransactionById(long id, Transaction newTransaction) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        if (transaction.isPresent()) {
            transaction.get().setCategory(newTransaction.getCategory());
            transaction.get().setOperationType(newTransaction.getOperationType());
            transaction.get().setSum(newTransaction.getSum());
            transaction.get().setDescription(newTransaction.getDescription());
            transaction.get().setDate(newTransaction.getDate());
            transaction.get().setTag(newTransaction.getTag());

            transactionRepository.save(transaction.get());
            return true;
        }
        return false;
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public void deleteAllTransactions() {
        transactionRepository.deleteAll();
    }

    @Override
    public List<ReportDayByDayDTO> generateDayByDayReport(OperationType operationType, String from, String to) throws ParseException {
        return transactionRepository.totalSumDayByDay(operationType, formatter.parse(from), formatter.parse(to));
    }

    @Override
    public List<ReportByCategoriesDTO> generateReportByCategories(OperationType operationType, String from, String to) throws ParseException {
        return transactionRepository.totalSumByCategories(operationType, formatter.parse(from), formatter.parse(to));
    }

    @Override
    public Double getTotalSumBetweenDays(OperationType operationType, String from, String to) throws ParseException {
        return transactionRepository.totalSumBetweenDays(operationType, formatter.parse(from), formatter.parse(to));
    }

    @Override
    public List<Integer> getYearsBetweenTwoDates(Long categoryId, OperationType operationType, String dateFrom, String dateTo) throws ParseException {
        return transactionRepository.getYearsBetweenDates(categoryId, operationType.ordinal(), formatter.parse(dateFrom), formatter.parse(dateTo));
    }

    @Override
    public List<ReportCostDynamicsForCategoryDTO> getTotalSumByMonthForCategory(Long categoryId, OperationType operationType, Integer year) {
        return transactionRepository.totalSumByMonthForCategory(categoryId, operationType.ordinal(), year);
    }

    @Override
    public List<ReportCostDynamicsForCategoryDTO> generateCostDynamicsReportForCategory(Long categoryId, OperationType operationType, String dateFrom, String dateTo) throws ParseException {

        List<Integer> years = getYearsBetweenTwoDates(categoryId, operationType, dateFrom, dateTo);
        List<ReportCostDynamicsForCategoryDTO> resultList = new ArrayList<>();

        for (Integer year : years) {
            resultList.addAll(getTotalSumByMonthForCategory(categoryId, operationType, year));
        }

        return resultList;
    }

    @Override
    public Page<Transaction> getAllTransactionForPageByCategory(Integer pageNo, Category category) {
        Pageable paging = PageRequest.of(pageNo, PAGE_SIZE_PAGINATION);

        return transactionRepository.findAllByCategory(category, paging);
    }
}
