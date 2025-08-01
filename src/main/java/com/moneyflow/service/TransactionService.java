package com.moneyflow.service;

import com.moneyflow.dto.TransactionRequestDTO;
import com.moneyflow.dto.TransactionResponseDTO;
import com.moneyflow.entity.Transaction;
import com.moneyflow.entity.Wallet;
import com.moneyflow.entity.enuns.TransactionType;
import com.moneyflow.repository.TransactionRepository;
import com.moneyflow.repository.WalletRepository;
import com.moneyflow.service.exception.DatabaseException;
import com.moneyflow.service.exception.ResourceNotFoundException;
import com.moneyflow.service.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public TransactionResponseDTO insert(TransactionRequestDTO dto) {
        Transaction transaction = updateEntityFromDTO(new Transaction(), dto);

        if(!walletRepository.existsById(dto.getWalletId()))
            throw new ResourceNotFoundException("Carteira com ID " +dto.getWalletId() + " não localizado!");

        Wallet wallet = walletRepository.getReferenceById(dto.getWalletId());
        transaction.setWallet(wallet);

        transaction = transactionRepository.save(transaction);
        return new TransactionResponseDTO(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionResponseDTO findById(UUID id){
        Optional<Transaction> result = transactionRepository.findById(id);
        Transaction transaction = result.orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        return new TransactionResponseDTO(transaction);
    }
    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> findAll(Pageable pageable) {
       Page<Transaction> transactions = transactionRepository.findAll(pageable);
       return transactions.map(t -> new TransactionResponseDTO(t));
    }

    @Transactional
    public TransactionResponseDTO update(UUID id, TransactionRequestDTO transactionRequestDTO) {
        Optional<Transaction> result = transactionRepository.findById(id);
        Transaction transaction = result.orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        if(transactionRequestDTO.getType() == TransactionType.INCOME){
            if(transactionRequestDTO.getCategoryIncome() == null || transactionRequestDTO.getCategoryExpense() != null){
                throw new ValidationException("Category Income is required for income transactions");
            }
        }

        if(transactionRequestDTO.getType() == TransactionType.EXPENSE){
            if(transactionRequestDTO.getCategoryExpense() == null || transactionRequestDTO.getCategoryIncome() != null){
                throw new ValidationException("Category Expense is required for expense transactions");
            }
        }

        Transaction transactionUpdated = updateEntityFromDTO(transaction,transactionRequestDTO);
        transactionUpdated = transactionRepository.save(transactionUpdated);

        return new TransactionResponseDTO(transactionUpdated);
    }

    @Transactional
    public void delete(UUID id) {
        if(!transactionRepository.findById(id).isPresent()){
            throw new ResourceNotFoundException("Transação não localizada!");
        }
        try{
            transactionRepository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }

    }

    private Transaction updateEntityFromDTO(Transaction transaction, TransactionRequestDTO dto) {
        if(dto.getDescription() != null) transaction.setDescription(dto.getDescription());
        if(dto.getAmount() != null) transaction.setAmount(dto.getAmount());
        if(dto.getTransactionDate() != null) transaction.setTransactionDate(dto.getTransactionDate());
        if(dto.getType() != null) transaction.setType(dto.getType());

        if(dto.getType() == TransactionType.EXPENSE)
            transaction.setCategoryIncome(null);
        else
        if(dto.getCategoryIncome() != null) transaction.setCategoryIncome(dto.getCategoryIncome());

        if(dto.getType() == TransactionType.INCOME)
            transaction.setCategoryExpense(null);
        else
        if(dto.getCategoryExpense() != null) transaction.setCategoryExpense(dto.getCategoryExpense());

        if(dto.getObservation() != null) transaction.setObservation(dto.getObservation());

        if(dto.getIsRealized()){
            transaction.setIsRealized(true);
            transaction.setRealizedDate(dto.getRealizedDate());
        }else{
            transaction.setIsRealized(false);
            transaction.setRealizedDate(null);
        }

        return transaction;
    }
}
