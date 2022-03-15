package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    public void createTransaction(Transaction transaction);

    public List<Transaction> getAllTransactions();

    public Optional<Transaction> getTransactionById(Long id);

    public void updateTransactionById(long id, Transaction newTransaction);

    public void deleteTransactionById(Long id);

    public void deleteAllTransactions();
}
