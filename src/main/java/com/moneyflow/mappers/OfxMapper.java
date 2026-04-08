package com.moneyflow.mappers;

import com.moneyflow.dto.OfxTransactionDTO;
import com.moneyflow.entity.enuns.OfxStatus;
import com.moneyflow.entity.enuns.OfxType;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.domain.data.common.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;

@Component
public class OfxMapper {
    private static final Logger log = LoggerFactory.getLogger(OfxMapper.class);

    public OfxTransactionDTO transactionOfxToDTO(Transaction transaction){
        return new OfxTransactionDTO(
                transaction.getId(),
                transaction.getDatePosted()
                        .toInstant()
                        .atZone(ZoneId.of("America/Sao_Paulo"))
                        .toLocalDate(),
                transaction.getMemo() != null
                        ? transaction.getMemo().trim().replaceAll("\\s+", " ")
                        : "",
                BigDecimal.valueOf(transaction.getAmount()),
                resolverTipo(transaction),
                OfxStatus.NEW
        );
    }

    private OfxType resolverTipo(Transaction transaction) {
        if(transaction.getTransactionType() == null){
            log.warn("TransactionType nulo para fitId={}, usando amount como fallback", transaction.getId());
            return transaction.getAmount() < 0 ? OfxType.DEBIT : OfxType.CREDIT;
        }
        try {
            TransactionType tipo = transaction.getTransactionType();
            return switch (tipo) {
                case DEBIT, ATM, POS, CHECK, PAYMENT, CASH -> OfxType.DEBIT;
                case CREDIT, DEP, DIRECTDEP -> OfxType.CREDIT;
                default -> {
                    log.warn("TransactionType desconhecido '{}' para fitId={}, usando amount como fallback",
                            tipo, transaction.getId());
                    yield transaction.getAmount() < 0 ? OfxType.DEBIT : OfxType.CREDIT;
                }
            };
        } catch (Exception e) {
            log.warn("Erro ao resolver TransactionType para fitId={}, usando amount como fallback",
                    transaction.getId(), e);
            return transaction.getAmount() < 0 ? OfxType.DEBIT : OfxType.CREDIT;
        }
    }
}
