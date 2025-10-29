package com.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TransactionDashFilterDTO {

    @NotNull(message = "Obrigatório informar data de início")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionStartDate;

    @NotNull(message = "Obrigatório informar data de fim")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionEndDate;

    public TransactionDashFilterDTO() { }

    public TransactionDashFilterDTO(LocalDate transactionStartDate, LocalDate transactionEndDate) {
        this.transactionStartDate = transactionStartDate;
        this.transactionEndDate = transactionEndDate;
    }

    public LocalDate getTransactionStartDate() {
        return transactionStartDate;
    }

    public void setTransactionStartDate(LocalDate transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }

    public LocalDate getTransactionEndDate() {
        return transactionEndDate;
    }

    public void setTransactionEndDate(LocalDate transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }
}
