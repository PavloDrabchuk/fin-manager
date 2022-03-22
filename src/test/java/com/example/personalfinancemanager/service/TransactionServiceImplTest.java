package com.example.personalfinancemanager.service;

import com.example.personalfinancemanager.dto.ReportByCategoriesDTO;
import com.example.personalfinancemanager.dto.ReportDayByDayDTO;
import com.example.personalfinancemanager.dto.impl.ReportByCategoriesDTOImpl;
import com.example.personalfinancemanager.dto.impl.ReportDayByDayDTOImpl;
import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import com.example.personalfinancemanager.repository.TransactionRepository;
import com.example.personalfinancemanager.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    private final Category category = new Category("Одяг", "Опис категорії \"Одяг\"");
    private final Category newCategory = new Category("Продукти", "Опис категорії \"Продукти\"");

    private final Date date = new Date(1647820800000L);

    private final Date newDate = new Date(1647648000000L);

    String pattern = "yyyy-MM-dd";
    DateFormat df = new SimpleDateFormat(pattern);

    Date from = new Date(1647648000000L - 7200000L),
            to = new Date(1647820800000L - 7200000L);

    @Test
    void testCreateOrSaveTransaction() {
        Transaction transaction = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);

        transactionService.createTransaction(transaction);

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);
        Transaction transaction2 = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис другої транзакції",
                date,
                null);

        transactions.add(transaction1);
        transactions.add(transaction2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> transactionList = transactionService.getAllTransactions();

        assertEquals(2, transactionList.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTransactionsForPage() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);
        Transaction transaction2 = new Transaction(
                category,
                OperationType.Revenue,
                678.90,
                "Опис другої транзакції",
                date,
                null);

        transactions.add(transaction1);
        transactions.add(transaction2);

        Page<Transaction> transactionPage = new PageImpl<>(transactions);

        Pageable paging = PageRequest.of(0, 7);

        when(transactionRepository.findAll(paging)).thenReturn(transactionPage);

        Page<Transaction> transactionsForPage = transactionService.getAllTransactionsForPage(0);

        assertAll("transactionsForPage",
                () -> assertEquals(1, transactionsForPage.getTotalPages()),
                () -> assertEquals(2, transactionsForPage.getTotalElements()),
                () -> assertEquals(transactions, transactionsForPage.getContent()));

        verify(transactionRepository, times(1)).findAll(paging);
    }

    @Test
    void testGetTransactionById() {
        Optional<Transaction> transaction1 = Optional.of(
                new Transaction(
                        category,
                        OperationType.Revenue,
                        123.45,
                        "Опис першої транзакції",
                        date,
                        null));

        when(transactionRepository.findById(1L)).thenReturn(transaction1);

        Optional<Transaction> transaction = transactionService.getTransactionById(1L);

        transaction.ifPresent(value -> assertAll("transaction",
                () -> assertEquals(category, transaction.get().getCategory()),
                () -> assertEquals(OperationType.Revenue, transaction.get().getOperationType()),
                () -> assertEquals(123.45, transaction.get().getSum()),
                () -> assertEquals("Опис першої транзакції", transaction.get().getDescription()),
                () -> assertEquals(date, transaction.get().getDate()),
                () -> assertNull(transaction.get().getTag())
        ));

        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTransactionById() {
        Optional<Transaction> transaction = Optional.of(
                new Transaction(
                        category,
                        OperationType.Expense,
                        123.45,
                        "Опис першої транзакції",
                        date,
                        null));

        Transaction newTransaction = new Transaction(
                newCategory,
                OperationType.Revenue,
                678.90,
                "Оновлений опис першої транзакції",
                newDate,
                "Подарунок");

        when(transactionRepository.findById(1L)).thenReturn(transaction);

        boolean updateAnswer = transactionService.updateTransactionById(1L, newTransaction);

        Optional<Transaction> updatedTransaction = transactionService.getTransactionById(1L);

        updatedTransaction.ifPresent(value -> assertAll("updatedTransaction",
                () -> assertTrue(updateAnswer),
                () -> assertEquals(newCategory, value.getCategory()),
                () -> assertEquals(OperationType.Revenue, value.getOperationType()),
                () -> assertEquals(678.90, value.getSum()),
                () -> assertEquals("Оновлений опис першої транзакції", value.getDescription()),
                () -> assertEquals(newDate, value.getDate()),
                () -> assertEquals("Подарунок", value.getTag())
        ));

        verify(transactionRepository, times(2)).findById(1L);
    }

    @Test
    void testFailedUpdateTransactionById() {
        Transaction newTransaction = new Transaction(
                newCategory,
                OperationType.Revenue,
                678.90,
                "Оновлений опис першої транзакції",
                newDate,
                "Подарунок");

        boolean updateAnswer = transactionService.updateTransactionById(2L, newTransaction);

        assertFalse(updateAnswer);
    }

    @Test
    void testDeleteTransactionById() {
        Transaction transaction = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);

        transactionService.deleteTransactionById(1L);

        Optional<Transaction> transaction1 = transactionService.getTransactionById(1L);

        assertTrue(transaction1.isEmpty());

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAllTransactionsById() {

        Transaction transaction1 = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);
        Transaction transaction2 = new Transaction(
                category,
                OperationType.Revenue,
                678.90,
                "Опис другої транзакції",
                date,
                null);

        transactionService.deleteAllTransactions();

        List<Transaction> transactionsAfterDeletion = transactionService.getAllTransactions();

        assertTrue(transactionsAfterDeletion.isEmpty());

        verify(transactionRepository, times(1)).deleteAll();
    }

    @Test
    void testGenerateDayByDayReport() throws ParseException {

        Transaction transaction1 = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);
        Transaction transaction2 = new Transaction(
                category,
                OperationType.Revenue,
                678.90,
                "Опис другої транзакції",
                newDate,
                null);

        List<ReportDayByDayDTO> reportDayByDayList = new ArrayList<>();
        reportDayByDayList.add(new ReportDayByDayDTOImpl(newDate, 678.90));
        reportDayByDayList.add(new ReportDayByDayDTOImpl(date, 123.45));

        when(transactionRepository.totalSumDayByDay(OperationType.Revenue, from, to)).thenReturn(reportDayByDayList);

        List<ReportDayByDayDTO> reportDayByDay = transactionService.generateDayByDayReport(
                OperationType.Revenue, df.format(newDate), df.format(date));

        assertAll("reportDayByDay",
                () -> assertFalse(reportDayByDay.isEmpty()),
                () -> assertEquals(2, reportDayByDay.size()),
                () -> assertEquals(newDate, reportDayByDay.get(0).getDate()),
                () -> assertEquals(678.90, reportDayByDay.get(0).getTotalSum()),
                () -> assertEquals(date, reportDayByDay.get(1).getDate()),
                () -> assertEquals(123.45, reportDayByDay.get(1).getTotalSum()));

        verify(transactionRepository, times(1)).totalSumDayByDay(OperationType.Revenue, from, to);
    }

    @Test
    void testGenerateReportByCategories() throws ParseException {

        Transaction transaction1 = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);
        Transaction transaction2 = new Transaction(
                newCategory,
                OperationType.Revenue,
                678.90,
                "Опис другої транзакції",
                newDate,
                null);

        List<ReportByCategoriesDTO> reportByCategoriesList = new ArrayList<>();
        reportByCategoriesList.add(new ReportByCategoriesDTOImpl(category, 123.45));
        reportByCategoriesList.add(new ReportByCategoriesDTOImpl(newCategory, 678.90));

        when(transactionRepository.totalSumByCategories(OperationType.Revenue, from, to)).thenReturn(reportByCategoriesList);

        List<ReportByCategoriesDTO> reportByCategories = transactionService.generateReportByCategories(
                OperationType.Revenue, df.format(newDate), df.format(date));

        assertAll("reportByCategories",
                () -> assertFalse(reportByCategories.isEmpty()),
                () -> assertEquals(2, reportByCategories.size()),
                () -> assertEquals(category, reportByCategories.get(0).getCategory()),
                () -> assertEquals(123.45, reportByCategories.get(0).getTotalSum()),
                () -> assertEquals(newCategory, reportByCategories.get(1).getCategory()),
                () -> assertEquals(678.90, reportByCategories.get(1).getTotalSum()));

        verify(transactionRepository, times(1)).totalSumByCategories(OperationType.Revenue, from, to);
    }

    @Test
    void testGetTotalSumBetweenDays() throws ParseException {

        Transaction transaction1 = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);
        Transaction transaction2 = new Transaction(
                category,
                OperationType.Revenue,
                678.90,
                "Опис другої транзакції",
                newDate,
                null);

        when(transactionRepository.totalSumBetweenDays(OperationType.Revenue, from, to)).thenReturn(802.35);

        Double totalSum = transactionService.getTotalSumBetweenDays(
                OperationType.Revenue, df.format(newDate), df.format(date));

        assertEquals(totalSum, 802.35);

        verify(transactionRepository, times(1)).totalSumBetweenDays(OperationType.Revenue, from, to);
    }
}
