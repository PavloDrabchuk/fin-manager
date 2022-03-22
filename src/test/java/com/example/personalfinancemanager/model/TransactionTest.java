package com.example.personalfinancemanager.model;

import com.example.personalfinancemanager.enums.OperationType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private final Category category = new Category("Одяг", "Опис категорії \"Одяг\"");
    private final Date date = new Date(1647888296000L);

    @Test
    void testTransactionConstructor() {
        Transaction transaction = new Transaction(
                category,
                OperationType.Revenue,
                123.45,
                "Опис першої транзакції",
                date,
                null);

        assertAll("transaction",
                () -> assertEquals(category, transaction.getCategory()),
                () -> assertEquals(OperationType.Revenue, transaction.getOperationType()),
                () -> assertEquals(123.45, transaction.getSum()),
                () -> assertEquals("Опис першої транзакції", transaction.getDescription()),
                () -> assertEquals(date, transaction.getDate()),
                () -> assertNull(transaction.getTag())
        );
    }

    @Test
    void testCategoryDefaultConstructor() {
        Transaction transaction = new Transaction();

        transaction.setId(1L);
        transaction.setCategory(category);
        transaction.setOperationType(OperationType.Revenue);
        transaction.setSum(123.45);
        transaction.setDescription("Опис першої транзакції");
        transaction.setDate(date);
        transaction.setTag(null);

        assertAll("transaction",
                () -> assertEquals(1L, transaction.getId()),
                () -> assertEquals(category, transaction.getCategory()),
                () -> assertEquals(OperationType.Revenue, transaction.getOperationType()),
                () -> assertEquals(123.45, transaction.getSum()),
                () -> assertEquals("Опис першої транзакції", transaction.getDescription()),
                () -> assertEquals(date, transaction.getDate()),
                () -> assertNull(transaction.getTag())
        );
    }
}
