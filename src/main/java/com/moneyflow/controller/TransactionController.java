package com.moneyflow.controller;

import com.moneyflow.dto.TransactionDTO;
import com.moneyflow.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("moneyflow")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("transactions")
    public List<TransactionDTO> getAllTransactions(){
        return transactionService.getAllTransactions();
    }
}
