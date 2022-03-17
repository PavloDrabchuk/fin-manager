package com.example.personalfinancemanager.service.impl;

import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import com.example.personalfinancemanager.repository.TransactionRepository;
import com.example.personalfinancemanager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final Integer PAGE_SIZE_PAGINATION = 3;

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
        if (pageNo < 0) pageNo = 0;

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
}
