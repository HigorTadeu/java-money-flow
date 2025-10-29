package com.moneyflow.repository;

import com.moneyflow.dto.TransactionDashResponseDTO;
import com.moneyflow.entity.Transaction;
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

    @Query(value = """
            select
            (select SUM(t.amount)
            from public.transactions t
            where t.type = 'INCOME'
            AND t.transaction_date BETWEEN :transactionStartDate ::DATE AND :transactionEndDate ::DATE) as total_income,
            (select SUM(t1.amount)
            from public.transactions t1
            where t1.type = 'INCOME'
            AND t1.transaction_date BETWEEN :transactionStartDate ::DATE AND :transactionEndDate ::DATE
            AND t1.is_realized = true ) as total_income_realized,
            (select SUM(t2.amount)
            from public.transactions t2
            where t2.type = 'EXPENSE'
            AND t2.transaction_date BETWEEN :transactionStartDate ::DATE AND :transactionEndDate ::DATE) as total_expense,
            (select SUM(t3.amount)
            from public.transactions t3
            where t3.type = 'EXPENSE'
            AND t3.transaction_date BETWEEN :transactionStartDate ::DATE AND :transactionEndDate ::DATE
            AND t3.is_realized = true ) as total_expense_realized
            ;
            """,
    nativeQuery = true)
    TransactionDashResponseDTO findTransactionInfoDash(@Param("transactionStartDate") LocalDate transactionStartDate,
                                                       @Param("transactionEndDate") LocalDate transactionEndDate);

    List<Transaction> findByTransactionDate(LocalDate date);

    List<Transaction> findByTransactionDateBetween(LocalDate start, LocalDate end);

    List<Transaction> findByTransactionDateAndIsRealized(LocalDate date, Boolean isRealized);
}