package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    void createTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();

    Optional<Transaction> getTransactionById(Long id);

    boolean updateTransactionById(long id, Transaction newTransaction);

    void deleteTransactionById(Long id);

    void deleteAllTransactions();
}
