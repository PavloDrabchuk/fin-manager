package com.example.personalfinancemanager.repository;

import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    @Query("select t.date, sum(t.sum) from Transaction t " +
            "where t.operationType = ?1 and t.date between ?2 and ? 3 " +
            "group by t.date " +
            "order by t.date")
    List<Object[]> totalSumDayByDay(OperationType operationType, Date from, Date to);

    @Query("select t.category, sum(t.sum) from Transaction t " +
            "where t.operationType = ?1 and t.date between ?1 and ?2 " +
            "group by t.category")
    List<Object[]> totalByCategories(OperationType operationType, Date from, Date to);
}
