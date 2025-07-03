package com.moneyflow.model;

import com.moneyflow.entity.enuns.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Description is required")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Value must be greater than zero")
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Transaction date is required")
    @Column(nullable = false)
    private LocalDate transaction_date;

    @NotNull(message = "Type of transaction is required")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String observation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDate getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(LocalDate transaction_date) {
        this.transaction_date = transaction_date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
