package com.moneyflow.controller;

import com.moneyflow.dto.TransactionDTO;
import com.moneyflow.dto.TransactionRequestDTO;
import com.moneyflow.dto.TransactionResponseDTO;
import com.moneyflow.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<TransactionDTO> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable UUID id){
        TransactionResponseDTO response = transactionService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> insert(@RequestBody TransactionRequestDTO transactionRequestDTO){
        TransactionResponseDTO transactionResponseDTO = transactionService.insert(transactionRequestDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionResponseDTO.getId())
                .toUri();
        return ResponseEntity.ok(transactionResponseDTO);
    }
}
