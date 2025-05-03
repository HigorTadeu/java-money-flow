package com.moneyflow.service;

import com.moneyflow.dto.TransactionDTO;
import com.moneyflow.model.Transaction;
import com.moneyflow.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionDTO> getAllTransactions(){
        return dataConverter(transactionRepository.findAll());
    }

    private List<TransactionDTO> dataConverter(List<Transaction> transactions){
        return transactions.stream()
                .map(t -> new TransactionDTO(t.getId(), t.getDescription(), t.getAmount(), t.getTransaction_date(), t.getType(), t.getCategory(), t.getObservation()))
                .collect(Collectors.toList());
    }
}
