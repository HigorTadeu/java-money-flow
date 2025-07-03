package com.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moneyflow.entity.enuns.CategoryExpense;
import com.moneyflow.entity.enuns.CategoryIncome;
import com.moneyflow.entity.enuns.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransactionRequestDTO {
    private UUID id;
    private String description;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
    private TransactionType type;
    private CategoryIncome categoryIncome;
    private CategoryExpense categoryExpense;
    private String observation;

    public TransactionRequestDTO() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public CategoryIncome getCategoryIncome() {
        return categoryIncome;
    }

    public void setCategoryIncome(CategoryIncome categoryIncome) {
        this.categoryIncome = categoryIncome;
    }

    public CategoryExpense getCategoryExpense() {
        return categoryExpense;
    }

    public void setCategoryExpense(CategoryExpense categoryExpense) {
        this.categoryExpense = categoryExpense;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
