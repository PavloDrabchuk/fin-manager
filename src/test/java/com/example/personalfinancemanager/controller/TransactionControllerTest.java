package com.example.personalfinancemanager.controller;

import com.example.personalfinancemanager.dto.ReportCostDynamicsForCategoryDTO;
import com.example.personalfinancemanager.enums.OperationType;
import com.example.personalfinancemanager.model.Category;
import com.example.personalfinancemanager.model.Transaction;
import com.example.personalfinancemanager.service.impl.CategoryServiceImpl;
import com.example.personalfinancemanager.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionServiceImpl transactionService;

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private TransactionController transactionController;

    private final Category category1 = new Category("Одяг", "Опис категорії \"Одяг\"");
    private final Category category2 = new Category("Продукти", "Опис категорії \"Продукти\"");

    private final Date date1 = new Date(1647820800000L);
    private final Date date2 = new Date(1647648000000L);

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction1 = new Transaction(
                category1,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date1,
                null);
        Transaction transaction2 = new Transaction(
                category1,
                OperationType.Revenue,
                123.45,
                "Опис другої транзакції",
                date1,
                null);

        transactions.add(transaction1);
        transactions.add(transaction2);

        Page<Transaction> transactionPage = new PageImpl<>(transactions);

        when(transactionService.getAllTransactionsForPage(0)).thenReturn(transactionPage);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/transactions"))
                .andExpect(forwardedUrl("transaction/transactions"))
                .andExpect(model().attribute("transactions", is(transactionPage)))
                .andExpect(model().attribute("page", is(0)));

        verify(transactionService, times(1)).getAllTransactionsForPage(0);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testGetAllTransactionsWithCategoryFilter() throws Exception {
        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction1 = new Transaction(
                category1,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date1,
                null);

        transactions.add(transaction1);

        Page<Transaction> transactionPage = new PageImpl<>(transactions);

        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category1));
        when(transactionService.getAllTransactionForPageByCategory(0,categoryService.getCategoryById(1L).get())).thenReturn(transactionPage);

        mockMvc.perform(get("/transactions")
                        .param("categoryId","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/transactions"))
                .andExpect(forwardedUrl("transaction/transactions"))
                .andExpect(model().attribute("transactions", is(transactionPage)))
                .andExpect(model().attribute("page", is(0)));

        verify(transactionService, times(1)).getAllTransactionForPageByCategory(0, category1);
        verify(categoryService,times(2)).getCategoryById(1L);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testTransactionForm() throws Exception {

        mockMvc.perform(get("/transactions/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/new-update-transaction"))
                .andExpect(forwardedUrl("transaction/new-update-transaction"));

        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testTransactionSubmit() throws Exception {

        Transaction transaction = new Transaction(
                category1,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date1,
                null);

        doNothing().when(transactionService).createTransaction(isA(Transaction.class));
        transactionService.createTransaction(transaction);

        mockMvc.perform(post("/transactions/new")
                        .flashAttr("transaction", transaction)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions"))
                .andExpect(flash().attribute("successTransactionSubmitMessage", "Транзакцію додано."));

        verify(transactionService, times(2)).createTransaction(transaction);
    }

    @Test
    void testTransactionSubmitWithErrors() throws Exception {
        Transaction transaction = new Transaction(null, null, -123.45, null, null, null);

        doNothing().when(transactionService).createTransaction(isA(Transaction.class));
        transactionService.createTransaction(transaction);

        mockMvc.perform(post("/transactions/new")
                        .flashAttr("transaction", transaction)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/new-update-transaction"))
                .andExpect(forwardedUrl("transaction/new-update-transaction"))
                .andExpect(model().attributeHasFieldErrors("transaction", "category"))
                .andExpect(model().attributeHasFieldErrors("transaction", "operationType"))
                .andExpect(model().attributeHasFieldErrors("transaction", "sum"))
                .andExpect(model().attributeHasFieldErrors("transaction", "description"))
                .andExpect(model().attributeHasFieldErrors("transaction", "date"))
                .andExpect(model().attribute("transaction", is(transaction)))
                .andExpect(model().attribute("categories", is(categoryService.getAllCategories())))
                .andExpect(model().attribute("operationTypes", is(Arrays.asList(OperationType.values()))));
    }

    @Test
    void testUpdateTransactionForm() throws Exception {
        Optional<Transaction> transaction = Optional.of(new Transaction(
                category1,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date1,
                null));

        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/transactions/{id}/update", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/new-update-transaction"))
                .andExpect(forwardedUrl("transaction/new-update-transaction"))
                .andExpect(model().attribute("transaction", hasProperty("category", is(category1))))
                .andExpect(model().attribute("transaction", hasProperty("operationType", is(OperationType.Revenue))))
                .andExpect(model().attribute("transaction", hasProperty("sum", is(123.45))))
                .andExpect(model().attribute("transaction", hasProperty("description", is("Опис першої транзакції"))))
                .andExpect(model().attribute("transaction", hasProperty("date", is(date1))))
                .andExpect(model().attribute("transaction", hasProperty("tag", nullValue())))
                .andExpect(model().attribute("updateTransaction", is(true)));

        verify(transactionService, times(1)).getTransactionById(1L);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testFailedUpdateTransactionForm() throws Exception {
        mockMvc.perform(get("/transactions/{id}/update", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions"))
                .andExpect(flash().attribute("failureTransactionMessage", is("Сталась помилка, спробуйте пізніше.")));

        verify(transactionService, times(1)).getTransactionById(1L);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testUpdateTransaction() throws Exception {

        Optional<Transaction> transaction = Optional.of(new Transaction(
                category1,
                OperationType.Expense,
                123.45,
                "Опис першої транзакції",
                date1,
                null));

        Transaction newTransaction = new Transaction(
                category2,
                OperationType.Revenue,
                678.90,
                "Оновлений опис першої транзакції",
                date2,
                "Подарунок");

        when(transactionService.updateTransactionById(1L, newTransaction)).thenReturn(true);

        mockMvc.perform(put("/transactions/{id}/update", 1L)
                        .flashAttr("transaction", newTransaction)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions"))
                .andExpect(flash().attribute("successTransactionUpdateMessage", is("Транзакцію оновлено.")));

        verify(transactionService, times(1)).updateTransactionById(1L, newTransaction);
    }

    @Test
    void testUpdateTransactionWithErrors() throws Exception {

        Optional<Transaction> transaction = Optional.of(new Transaction(
                category1,
                OperationType.Expense,
                123.45,
                "Опис першої транзакції",
                date1,
                null));

        Transaction newTransaction = new Transaction(null, null, -678.90, null, null, null);

        mockMvc.perform(put("/transactions/{id}/update", 1L)
                        .flashAttr("transaction", newTransaction)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("transaction/new-update-transaction"))
                .andExpect(forwardedUrl("transaction/new-update-transaction"))
                .andExpect(model().attributeHasFieldErrors("transaction", "category"))
                .andExpect(model().attributeHasFieldErrors("transaction", "operationType"))
                .andExpect(model().attributeHasFieldErrors("transaction", "sum"))
                .andExpect(model().attributeHasFieldErrors("transaction", "description"))
                .andExpect(model().attributeHasFieldErrors("transaction", "date"))
                .andExpect(model().attribute("transaction", is(newTransaction)))
                .andExpect(model().attribute("categories", is(categoryService.getAllCategories())))
                .andExpect(model().attribute("operationTypes", is(Arrays.asList(OperationType.values()))))
                .andExpect(model().attribute("updateTransaction", is(true)));

        verifyNoInteractions(transactionService);
    }

    @Test
    void testFailedUpdateTransaction() throws Exception {

        Optional<Transaction> transaction = Optional.of(new Transaction(
                category1,
                OperationType.Expense,
                123.45,
                "Опис першої транзакції",
                date1,
                null));

        mockMvc.perform(put("/transactions/{id}/update", 1L)
                        .flashAttr("transaction", transaction)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions"))
                .andExpect(flash().attribute("failureTransactionMessage", is("Сталась помилка, спробуйте пізніше.")));
    }

    @Test
    void testDeleteTransaction() throws Exception {

        Optional<Transaction> transaction = Optional.of(new Transaction(
                category1,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date1,
                null));

        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(delete("/transactions/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions"))
                .andExpect(flash().attribute("successTransactionDeleteMessage", is("Транзакцію видалено.")));

        verify(transactionService, times(1)).getTransactionById(1L);
    }

    @Test
    void testFailedDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/transactions/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions"))
                .andExpect(flash().attribute("failureTransactionMessage", is("Сталась помилка, спробуйте пізніше.")));

        verify(transactionService, times(1)).getTransactionById(1L);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testReportGeneratorForm() throws Exception {

        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/transactions/report"))
                .andExpect(status().isOk())
                .andExpect(view().name("report/report-generator-form"))
                .andExpect(forwardedUrl("report/report-generator-form"))
                .andExpect(model().attribute("categories", is(Arrays.asList(category1, category2))))
                .andExpect(model().attribute("operationTypes", is(Arrays.asList(OperationType.values()))));

        verify(categoryService, times(1)).getAllCategories();
        verifyNoMoreInteractions(categoryService);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testGeneratedReport() throws Exception {
        mockMvc.perform(get("/transactions/report/view"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions/report"));
    }

    @Test
    void testGenerateReportByDay() throws Exception {

        String reportType = "ByDay";
        String dateFrom = "2020-01-01";
        String dateTo = "2025-01-01";
        OperationType operationType = OperationType.Revenue;

        mockMvc.perform(post("/transactions/report/view")
                        .param("reportType", reportType)
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
                        .param("operationType", String.valueOf(operationType))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("report/report"))
                .andExpect(forwardedUrl("report/report"))
                .andExpect(model().attribute("reportType", is(reportType)))
                .andExpect(model().attribute("dateFrom", is(dateFrom)))
                .andExpect(model().attribute("dateTo", is(dateTo)))
                .andExpect(model().attribute("operationType", is(operationType)))
                .andExpect(model().attribute("reportDayByDay", is(transactionService.generateDayByDayReport(operationType, dateFrom, dateTo))));
    }

    @Test
    void testGenerateReportByCategories() throws Exception {

        String reportType = "ByCategories";
        String dateFrom = "2020-01-01";
        String dateTo = "2025-01-01";
        OperationType operationType = OperationType.Revenue;

        mockMvc.perform(post("/transactions/report/view")
                        .param("reportType", reportType)
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
                        .param("operationType", String.valueOf(operationType))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("report/report"))
                .andExpect(forwardedUrl("report/report"))
                .andExpect(model().attribute("reportType", is(reportType)))
                .andExpect(model().attribute("dateFrom", is(dateFrom)))
                .andExpect(model().attribute("dateTo", is(dateTo)))
                .andExpect(model().attribute("operationType", is(operationType)))
                .andExpect(model().attribute("reportByCategories", is(transactionService.generateReportByCategories(operationType, dateFrom, dateTo))))
                .andExpect(model().attribute("totalSum", is(transactionService.getTotalSumBetweenDays(operationType, dateFrom, dateTo))));
    }

    @Test
    void testGenerateCostDynamicsReport() throws Exception {

        Optional<Category> category = Optional.of(new Category("Одяг", "Опис категорії \"Одяг\""));

        when(categoryService.getCategoryById(1L)).thenReturn(category);

        String reportType = "CostDynamics";
        String dateFrom = "2017-01-01";
        String dateTo = "2022-01-01";
        OperationType operationType = OperationType.Revenue;

        mockMvc.perform(post("/transactions/report/view")
                        .param("reportType", reportType)
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
                        .param("operationType", String.valueOf(operationType))
                        .param("category","1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("report/report"))
                .andExpect(forwardedUrl("report/report"))
                .andExpect(model().attribute("reportType", is(reportType)))
                .andExpect(model().attribute("dateFrom", is(dateFrom)))
                .andExpect(model().attribute("dateTo", is(dateTo)))
                .andExpect(model().attribute("operationType", is(operationType)))
                .andExpect(model().attribute("reportCostDynamicsForCategory", is(transactionService.generateCostDynamicsReportForCategory(1L,operationType, dateFrom, dateTo))))
                .andExpect(model().attribute("category", is(category.get())));

        verify(categoryService,times(1)).getCategoryById(1L);
    }

    @Test
    void testGenerateCostDynamicsReportWithoutCategory() throws Exception {

        String reportType = "CostDynamics";
        String dateFrom = "2017-01-01";
        String dateTo = "2022-01-01";
        OperationType operationType = OperationType.Revenue;

        mockMvc.perform(post("/transactions/report/view")
                        .param("reportType", reportType)
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
                        .param("operationType", String.valueOf(operationType))
                        .param("category","")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions/report"))
                .andExpect(flash().attribute("failureReportMessage", is("Оберіть категорію для формування звіту.")));
    }

    @Test
    void testFailedGenerateCostDynamicsReport() throws Exception {

        String reportType = "CostDynamics";
        String dateFrom = "2017-01-01";
        String dateTo = "2022-01-01";
        OperationType operationType = OperationType.Revenue;

        mockMvc.perform(post("/transactions/report/view")
                        .param("reportType", reportType)
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
                        .param("operationType", String.valueOf(operationType))
                        .param("category","1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions/report"))
                .andExpect(flash().attribute("failureReportMessage", is("Сталась помилка, спробуйте пізніше.")));
    }

    @Test
    void testDefaultGenerateReport() throws Exception {

        String reportType = "default";
        String dateFrom = "2020-01-01";
        String dateTo = "2025-01-01";
        OperationType operationType = OperationType.Revenue;

        mockMvc.perform(post("/transactions/report/view")
                        .param("reportType", reportType)
                        .param("dateFrom", dateFrom)
                        .param("dateTo", dateTo)
                        .param("operationType", String.valueOf(operationType))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions/report"))
                .andExpect(flash().attribute("failureReportMessage", "Сталась помилка, спробуйте пізніше."));
    }
}
