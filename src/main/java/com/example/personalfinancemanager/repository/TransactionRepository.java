package com.example.personalfinancemanager.repository;

import com.example.personalfinancemanager.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
}
