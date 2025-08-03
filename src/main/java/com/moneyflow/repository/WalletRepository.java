package com.moneyflow.repository;

import com.moneyflow.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Page<Wallet> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
