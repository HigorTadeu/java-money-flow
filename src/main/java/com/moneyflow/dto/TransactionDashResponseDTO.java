package com.moneyflow.dto;

import java.math.BigDecimal;

public class TransactionDashResponseDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalIncomeRealized;
    private BigDecimal totalExpense;
    private BigDecimal totalExpenseRealized;

    public TransactionDashResponseDTO() { }

    public TransactionDashResponseDTO(BigDecimal totalIncome, BigDecimal totalIncomeRealized, BigDecimal totalExpense, BigDecimal totalExpenseRealized) {
        this.totalIncome = totalIncome;
        this.totalIncomeRealized = totalIncomeRealized;
        this.totalExpense = totalExpense;
        this.totalExpenseRealized = totalExpenseRealized;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalIncomeRealized() {
        return totalIncomeRealized;
    }

    public void setTotalIncomeRealized(BigDecimal totalIncomeRealized) {
        this.totalIncomeRealized = totalIncomeRealized;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotalExpenseRealized() {
        return totalExpenseRealized;
    }

    public void setTotalExpenseRealized(BigDecimal totalExpenseRealized) {
        this.totalExpenseRealized = totalExpenseRealized;
    }
}
