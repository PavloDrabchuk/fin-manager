package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import org.springframework.data.domain.Page;

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
}
