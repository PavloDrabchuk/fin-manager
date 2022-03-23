package com.example.personalfinancemanager.model;

import com.example.personalfinancemanager.enums.OperationType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Оберіть категорію")
    private Category category;

    @Column(nullable = false)
    @NotNull(message = "Оберіть тип операції")
    private OperationType operationType;

    @Column(nullable = false)
    @NotNull(message = "Введіть суму")
    @Min(0)
    private Double sum;

    @Column(nullable = false)
    @NotBlank(message = "Введіть опис транзакції")
    @NotNull
    private String description;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Введіть дату")
    private Date date;

    @Column(length = 100)
    private String tag;

    public Transaction() {
    }

    public Transaction(Category category, OperationType operationType, Double sum, String description, Date date, String tag) {
        this.category = category;
        this.operationType = operationType;
        this.sum = sum;
        this.description = description;
        this.date = date;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
