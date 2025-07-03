package com.moneyflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moneyflow.entity.enuns.CategoryExpense;
import com.moneyflow.entity.enuns.CategoryIncome;
import com.moneyflow.entity.enuns.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionDTO(UUID id,
                             String description,
                             BigDecimal amount,
                             @JsonFormat(pattern = "yyyy-MM-dd") LocalDate transactionDate,
                             TransactionType type,
                             CategoryIncome categoryIncome,
                             CategoryExpense categoryExpense,
                             String observation
) {}
