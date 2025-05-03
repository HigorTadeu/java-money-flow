package com.moneyflow.dto;

import com.moneyflow.model.TransactionCategory;
import com.moneyflow.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(Long id,
                             String description,
                             BigDecimal amount,
                             LocalDate transaction_date,
                             TransactionType type,
                             TransactionCategory category,
                             String observation
) {}
