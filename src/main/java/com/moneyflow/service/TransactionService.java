package com.moneyflow.service;

import com.moneyflow.dto.TransactionDTO;
import com.moneyflow.dto.TransactionRequestDTO;
import com.moneyflow.dto.TransactionResponseDTO;
import com.moneyflow.entity.Transaction;
import com.moneyflow.repository.TransactionRepository;
import com.moneyflow.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionDTO> getAllTransactions() {
        return null;
    }

    @Transactional
    public TransactionResponseDTO insert(TransactionRequestDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setType(dto.getType());
        transaction.setCategoryIncome(dto.getCategoryIncome());
        transaction.setCategoryExpense(dto.getCategoryExpense());
        transaction.setObservation(dto.getObservation());

        transaction = transactionRepository.save(transaction);
        return new TransactionResponseDTO(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionResponseDTO findById(UUID id){
        Optional<Transaction> result = transactionRepository.findById(id);
        Transaction transaction = result.orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        return new TransactionResponseDTO(transaction);
    }

//    private void validateTransaction(Transaction transaction) {
//        // Validações de negócio
//        if (transaction.getType() == TransactionType.INCOME && transaction.getCategoryIncome() == null) {
//            throw new ValidationException("Category Income is required for income transactions");
//        }
//        if (transaction.getType() == TransactionType.EXPENSE && transaction.getCategoryExpense() == null) {
//            throw new ValidationException("Category Expense is required for expense transactions");
//        }
//    }
//
//    private void updateTransactionFromDTO(Transaction transaction, TransactionUpdateDTO dto) {
//        if (dto.getDescription() != null) transaction.setDescription(dto.getDescription());
//        if (dto.getAmount() != null) transaction.setAmount(dto.getAmount());
//        if (dto.getTransactionDate() != null) transaction.setTransactionDate(dto.getTransactionDate());
//        if (dto.getType() != null) transaction.setType(dto.getType());
//        if (dto.getObservation() != null) transaction.setObservation(dto.getObservation());
//        // ... outras atualizações
//    }



//    public List<TransactionDTO> getAllTransactions(){
//        return dataConverter(transactionRepository.findAll());
//    }
//
//    private List<TransactionDTO> dataConverter(List<Transaction> transactions){
//        return transactions.stream()
//                .map(t -> new TransactionDTO(t.getId(), t.getDescription(), t.getAmount(), t.getTransactionDate(), t.getType(), t.getCategory(), t.getObservation()))
//                .collect(Collectors.toList());
//    }
}
