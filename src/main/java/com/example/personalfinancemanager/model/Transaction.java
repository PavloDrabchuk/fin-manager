package com.example.personalfinancemanager.model;

import com.example.personalfinancemanager.enums.OperationType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    @Column(nullable = false)
    private OperationType operationType;

    @Column(nullable = false)
    private Double sum;

    @Column(nullable = false)
    private Date date;

    @Column(length = 100)
    private String tag;

    public Transaction() {
    }

    public Transaction(Category category, OperationType operationType, Double sum, Date date, String tag) {
        this.category = category;
        this.operationType = operationType;
        this.sum = sum;
        this.date = date;
        this.tag = tag;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
