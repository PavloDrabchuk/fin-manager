package com.example.personalfinancemanager.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OperationTypeTest {
    @Test
    void testOperationTypeConstructor() {

        OperationType revenueOperationType = OperationType.Revenue;
        OperationType expenseOperationType = OperationType.Expense;

        assertAll(
                () -> assertEquals("Дохід", revenueOperationType.getName()),
                () -> assertEquals("Витрата", expenseOperationType.getName())
        );
    }
}
