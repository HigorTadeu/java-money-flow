package com.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moneyflow.entity.enuns.CategoryExpense;
import com.moneyflow.entity.enuns.CategoryIncome;
import com.moneyflow.entity.enuns.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TransactionRequestDTO {
    private UUID id;
    @NotNull(message = "Description is required")
    private String description;
    @NotNull(message = "Amount is required")
    @Positive(message = "Value must be greater than zero")
    private BigDecimal amount;
    @NotNull(message = "Transaction date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;
    @NotNull(message = "Type of transaction is required")
    private TransactionType type;
    private CategoryIncome categoryIncome;
    private CategoryExpense categoryExpense;
    @Size(max = 255, message = "Max length 255 characters")
    private String observation;
    @NotNull(message = "Wallet is required")
    private UUID walletId;
    @NotNull(message = "É obrigatório informar se foi Realizado ou Não")
    private Boolean isRealized;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate realizedDate;

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

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public Boolean getIsRealized() {
        return isRealized;
    }

    public void setIsRealized(Boolean realized) {
        isRealized = realized;
    }

    public LocalDate getRealizedDate() {
        return realizedDate;
    }

    public void setRealizedDate(LocalDate realizedDate) {
        this.realizedDate = realizedDate;
    }
}
