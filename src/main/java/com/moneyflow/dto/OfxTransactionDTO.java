package com.moneyflow.dto;

import com.moneyflow.entity.enuns.OfxStatus;
import com.moneyflow.entity.enuns.OfxType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OfxTransactionDTO(
        String fitId,
        LocalDate transactionDate,
        String memo,
        BigDecimal transactionAmount,
        OfxType transactionType,
        OfxStatus status
) {
}
