package com.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moneyflow.entity.enuns.CategoryExpense;
import com.moneyflow.entity.enuns.CategoryIncome;
import com.moneyflow.entity.enuns.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionFilterDTO {
    private String description;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionEndDate;

    private TransactionType type;
    private CategoryIncome categoryIncome;
    private CategoryExpense categoryExpense;
    private Boolean isRealized;

    public TransactionFilterDTO() {}

    public TransactionFilterDTO(String description, BigDecimal minAmount, BigDecimal maxAmount,
                                LocalDate transactionStartDate, LocalDate transactionEndDate,
                                TransactionType type, CategoryIncome categoryIncome,
                                CategoryExpense categoryExpense, Boolean isRealized) {
        this.description = description;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.transactionStartDate = transactionStartDate;
        this.transactionEndDate = transactionEndDate;
        this.type = type;
        this.categoryIncome = categoryIncome;
        this.categoryExpense = categoryExpense;
        this.isRealized = isRealized;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public LocalDate getTransactionStartDate() {
        return transactionStartDate;
    }

    public LocalDate getTransactionEndDate() {
        return transactionEndDate;
    }

    public TransactionType getType() {
        return type;
    }

    public CategoryIncome getCategoryIncome() {
        return categoryIncome;
    }

    public CategoryExpense getCategoryExpense() {
        return categoryExpense;
    }

    public Boolean getIsRealized() {
        return isRealized;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setTransactionStartDate(LocalDate transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }

    public void setTransactionEndDate(LocalDate transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setCategoryIncome(CategoryIncome categoryIncome) {
        this.categoryIncome = categoryIncome;
    }

    public void setCategoryExpense(CategoryExpense categoryExpense) {
        this.categoryExpense = categoryExpense;
    }

    public void setIsRealized(Boolean realized) {
        this.isRealized = realized;
    }

    public boolean hasFilter(){
        return (description != null && !description.trim().isEmpty()) ||
                minAmount != null ||
                maxAmount != null ||
                transactionStartDate != null ||
                transactionEndDate != null ||
                type != null ||
                categoryIncome != null ||
                categoryExpense != null ||
                isRealized != null;
    }
}