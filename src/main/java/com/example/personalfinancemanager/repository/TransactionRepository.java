package com.example.personalfinancemanager.repository;

import com.example.personalfinancemanager.dto.ReportByCategoriesDTO;
import com.example.personalfinancemanager.dto.ReportCostDynamicsForCategoryDTO;
import com.example.personalfinancemanager.dto.ReportDayByDayDTO;
import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    @Query("select t.date as date, sum(t.sum) as totalSum from Transaction t " +
            "where t.operationType = ?1 and t.date between ?2 and ?3 " +
            "group by t.date " +
            "order by t.date")
    List<ReportDayByDayDTO> totalSumDayByDay(OperationType operationType, Date from, Date to);

    @Query("select t.category as category, sum(t.sum) as totalSum from Transaction t " +
            "where t.operationType = ?1 and t.date between ?2 and ?3 " +
            "group by t.category")
    List<ReportByCategoriesDTO> totalSumByCategories(OperationType operationType, Date from, Date to);

    @Query("select sum(t.sum) from Transaction t where t.operationType=?1 and t.date between ?2 and ?3")
    Double totalSumBetweenDays(OperationType operationType, Date from, Date to);


    @Query(value = "select extract(year from t.date) as `year`, extract(month from t.date) as `month`, sum(t.sum) as `totalSum` from Transaction t " +
            "where t.category_id = ?1 and t.operation_type = ?2 " +
            "and extract(year from t.date) in (select distinct extract(year from t1.date) from Transaction t1) " +
            "and t.date between ?3 and ?4 " +
            "group by extract( month from t.date) " +
            "order by t.date",
            nativeQuery = true)
    List<ReportCostDynamicsForCategoryDTO> totalSumByMonthForCategory(Long id,
                                                                      Integer operationType,
                                                                      Date from,
                                                                      Date to);
}
