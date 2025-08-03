package com.moneyflow.service;

import com.moneyflow.dto.WalletRequestDTO;
import com.moneyflow.dto.WalletResponseDTO;
import com.moneyflow.entity.Wallet;
import com.moneyflow.repository.WalletRepository;
import com.moneyflow.service.exception.DatabaseException;
import com.moneyflow.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public WalletResponseDTO findById(UUID id){
        Optional<Wallet> result = walletRepository.findById(id);
        Wallet wallet = result.orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: " + id));

        return new WalletResponseDTO(wallet);
    }

    @Transactional(readOnly = true)
    public Page<WalletResponseDTO> findAll(Pageable pageable){
        Page<Wallet> wallets = walletRepository.findAll(pageable);
        return  wallets.map(w -> new WalletResponseDTO(w));
    }

    @Transactional(readOnly = true)
    public List<WalletResponseDTO> findByName(String name) {
        List<Wallet> result = walletRepository.findByNameContainingIgnoreCase(name);

        return result.stream().map(w -> new WalletResponseDTO(w)).toList();
    }

    @Transactional
    public WalletResponseDTO insert(WalletRequestDTO dto) {
        Wallet wallet = new Wallet();
        wallet.setName(dto.getName());
        wallet.setDescription(dto.getDescription());
        wallet.setActive(dto.getActive());

        wallet = walletRepository.save(wallet);

        return new WalletResponseDTO(wallet);
    }

    @Transactional
    public WalletResponseDTO update(UUID id, WalletRequestDTO dto) {
        Optional<Wallet> result = walletRepository.findById(id);
        Wallet entity = result.orElseThrow(() -> new ResourceNotFoundException("Carteira não localizada! "+id));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getActive());

        Wallet walletUpdated = walletRepository.save(entity);
        return new WalletResponseDTO(walletUpdated);
    }

    @Transactional
    public void delete(UUID id){
        Optional<Wallet> result = walletRepository.findById(id);
        if(!result.isPresent()){
            throw new ResourceNotFoundException("Carteira não localizada: "+id);
        }

        try{
            walletRepository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha na integridade referencial");
        }
    }
}
