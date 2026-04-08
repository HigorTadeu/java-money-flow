package com.moneyflow.service;

import com.moneyflow.config.google.GoogleConnection;
import com.moneyflow.dto.TransactionDashFilterDTO;
import com.moneyflow.dto.TransactionDashResponseDTO;
import com.moneyflow.dto.TransactionFilterDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionService {

    private GoogleService googleService;

    TransactionService(GoogleService googleService){
        this.googleService = googleService;
    }

    @Value("${SHEET_IMPORT}")
    private String idSheet;

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
    public Page<TransactionResponseDTO> findByDescription(String description, Pageable pageable) {
        Page<Transaction> result = transactionRepository.findByDescriptionContainingIgnoreCase(description, pageable);
        return result.map(t -> new TransactionResponseDTO(t));
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> findByFilter(TransactionFilterDTO filter, Pageable pageable) {
        if (!filter.hasFilter()) {
            return findAll(pageable);
        }

        String typeString = filter.getType() != null ? filter.getType().name() : null;
        String categoryIncomeString = filter.getCategoryIncome() != null ? filter.getCategoryIncome().name() : null;
        String categoryExpenseString = filter.getCategoryExpense() != null ? filter.getCategoryExpense().name() : null;

        Page<Transaction> result = transactionRepository.findByFilter(
                filter.getDescription(),
                filter.getMinAmount(),
                filter.getMaxAmount(),
                typeString,
                categoryIncomeString,
                categoryExpenseString,
                filter.getTransactionStartDate(),
                filter.getTransactionEndDate(),
                filter.getIsRealized(),
                pageable);

        return result.map(t -> new TransactionResponseDTO(t));
    }

    @Transactional(readOnly = true)
    public TransactionDashResponseDTO getTransactionInfoDash(TransactionDashFilterDTO dto) {
        TransactionDashResponseDTO result =  transactionRepository.findTransactionInfoDash(dto.getTransactionStartDate(),dto.getTransactionEndDate());
        TransactionDashResponseDTO dash = new TransactionDashResponseDTO();

        if(result.getTotalIncome() == null){
            dash.setTotalIncome(BigDecimal.ZERO);
        }else{
            dash.setTotalIncome(result.getTotalIncome());
        }

        if(result.getTotalExpense() == null){
            dash.setTotalExpense(BigDecimal.ZERO);
        }else{
            dash.setTotalExpense(result.getTotalExpense());
        }

        if(result.getTotalIncomeRealized() == null){
            dash.setTotalIncomeRealized(BigDecimal.ZERO);
        }else{
            dash.setTotalIncomeRealized(result.getTotalIncomeRealized());
        }

        if(result.getTotalExpenseRealized()== null){
            dash.setTotalExpenseRealized(BigDecimal.ZERO);
        }else{
            dash.setTotalExpenseRealized(result.getTotalExpenseRealized());
        }

        return dash;
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

    public void importDataOfxFile(String aba, String range){
        String completeRange = aba + "!" + range;

        try{
            List<List<Object>> fullSheet = googleService.readSheet(idSheet, completeRange);
            List<List<Object>> filteredSheed = googleService.filterDatePeriod(fullSheet, LocalDate.of(2026,02,01),LocalDate.of(2026,02,28));
            if (filteredSheed != null && !filteredSheed.isEmpty()){
                int contY = 0;
                for(int i = 0; i < filteredSheed.size(); i++){
                    for(int y = 0; y < filteredSheed.get(i).size(); y++){
                        System.out.print(filteredSheed.get(i).get(y) + " | ");
                    }
                    System.out.println("");
                }
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        if(dto.getWalletId() != null && transaction.getWallet() != null){
            if(dto.getWalletId() != transaction.getWallet().getId()){
                Wallet wallet = walletRepository.getReferenceById(dto.getWalletId());
                transaction.setWallet(wallet);
            }
        }

        return transaction;
    }
}
