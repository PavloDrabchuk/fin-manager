package com.example.personalfinancemanager.enums;

public enum OperationType {
    Expense("Витрата"),
    Revenue("Дохід");

    private final String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
