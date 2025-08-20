package com.moneyflow.repository;

import com.moneyflow.entity.Transaction;
import com.moneyflow.entity.enuns.CategoryExpense;
import com.moneyflow.entity.enuns.CategoryIncome;
import com.moneyflow.entity.enuns.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

//    @Query("SELECT t FROM Transaction t " +
//            "WHERE (COALESCE(:description, '') = '' OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
//            "AND (:minAmount IS NULL OR t.amount >= :minAmount) " +
//            "AND (:maxAmount IS NULL OR t.amount <= :maxAmount) " +
//            "AND (:type IS NULL OR t.type = :type) " +
//            "AND (:categoryIncome IS NULL OR t.categoryIncome = :categoryIncome) " +
//            "AND (:categoryExpense IS NULL OR t.categoryExpense = :categoryExpense) " +
//            "AND (:transactionStartDate IS NULL OR t.transactionDate >= CAST(:transactionStartDate AS DATE)) " +
//            "AND (:transactionEndDate IS NULL OR t.transactionDate <= CAST(:transactionEndDate AS DATE)) " +
//            "AND (:realized IS NULL OR t.isRealized = CAST(:realized AS boolean)) " +
//            "ORDER BY t.transactionDate DESC")
//    Page<Transaction> findByFilter(
//            @Param("description") String description,
//            @Param("minAmount") BigDecimal minAmount,
//            @Param("maxAmount") BigDecimal maxAmount,
//            @Param("type") TransactionType type,
//            @Param("categoryIncome") CategoryIncome categoryIncome,
//            @Param("categoryExpense") CategoryExpense categoryExpense,
//            @Param("transactionStartDate") LocalDate transactionStartDate,
//            @Param("transactionEndDate") LocalDate transactionEndDate,
//            @Param("realized") Boolean realized,
//            Pageable pageable);

    @Query(value = "SELECT * FROM transactions t " +
            "WHERE (:description IS NULL OR :description = '' OR LOWER(t.description) LIKE LOWER(CONCAT('%', CAST(:description AS VARCHAR), '%'))) " +
            "AND (:minAmount IS NULL OR t.amount >= CAST(:minAmount AS DECIMAL)) " +
            "AND (:maxAmount IS NULL OR t.amount <= CAST(:maxAmount AS DECIMAL)) " +
           "AND (:type IS NULL OR t.type = CAST(:type AS VARCHAR)) " +
            "AND (:categoryIncome IS NULL OR t.category_income = CAST(:categoryIncome AS VARCHAR)) " +
            "AND (:categoryExpense IS NULL OR t.category_expense = CAST(:categoryExpense AS VARCHAR)) " +
            "AND (:transactionStartDate ::DATE IS NULL OR t.transaction_date >= :transactionStartDate ::DATE ) " +
            "AND (:transactionEndDate ::DATE IS NULL OR t.transaction_date <= :transactionEndDate ::DATE ) " +
            "AND (:realized ::BOOLEAN IS NULL OR t.is_realized = :realized ::BOOLEAN) " +
            "ORDER BY t.transaction_date DESC",
            nativeQuery = true)
    Page<Transaction> findByFilter(@Param("description") String description,
                                   @Param("minAmount") BigDecimal minAmount,
                                   @Param("maxAmount") BigDecimal maxAmount,
                                   @Param("type") String type,
                                   @Param("categoryIncome") String categoryIncome,
                                   @Param("categoryExpense") String categoryExpense,
                                   @Param("transactionStartDate") LocalDate transactionStartDate,
                                   @Param("transactionEndDate") LocalDate transactionEndDate,
                                   @Param("realized") Boolean realized,
                                   Pageable pageable);

    // Busca por data exata
    List<Transaction> findByTransactionDate(LocalDate date);

    // Busca por intervalo de datas
    List<Transaction> findByTransactionDateBetween(LocalDate start, LocalDate end);

    // Busca por data e realização
    List<Transaction> findByTransactionDateAndIsRealized(LocalDate date, Boolean isRealized);
}