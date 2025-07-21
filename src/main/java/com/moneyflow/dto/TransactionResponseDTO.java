package com.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moneyflow.entity.Transaction;
import com.moneyflow.entity.enuns.CategoryExpense;
import com.moneyflow.entity.enuns.CategoryIncome;
import com.moneyflow.entity.enuns.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransactionResponseDTO {

    private UUID id;
    private String description;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
    private TransactionType type;
    private CategoryIncome categoryIncome;
    private CategoryExpense categoryExpense;
    private String observation;
    private WalletDTO wallet;

    public TransactionResponseDTO() { }

    public TransactionResponseDTO(UUID id, String description, BigDecimal amount, LocalDate transactionDate, TransactionType type, CategoryIncome categoryIncome, CategoryExpense categoryExpense, String observation, WalletDTO wallet) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.type = type;
        this.categoryIncome = categoryIncome;
        this.categoryExpense = categoryExpense;
        this.observation = observation;
        this.wallet = wallet;
    }

    public TransactionResponseDTO(Transaction transaction){
        id = transaction.getId();
        description = transaction.getDescription();
        amount = transaction.getAmount();
        transactionDate = transaction.getTransactionDate();
        type = transaction.getType();
        categoryIncome = transaction.getCategoryIncome();
        categoryExpense = transaction.getCategoryExpense();
        observation = transaction.getObservation();
        wallet = new WalletDTO(transaction.getWallet());
    }

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

    public WalletDTO getWallet() {
        return wallet;
    }

    public void setWallet(WalletDTO wallet) {
        this.wallet = wallet;
    }
}
