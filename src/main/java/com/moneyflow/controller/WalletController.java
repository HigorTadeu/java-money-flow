package com.moneyflow.controller;

import com.moneyflow.dto.WalletRequestDTO;
import com.moneyflow.dto.WalletResponseDTO;
import com.moneyflow.repository.WalletRepository;
import com.moneyflow.service.WalletService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<WalletResponseDTO> findById(@PathVariable UUID id){
        WalletResponseDTO response = walletService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<WalletResponseDTO>> findAll(Pageable pageable){
        Page<WalletResponseDTO> response = walletService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/w")
    public ResponseEntity<List<WalletResponseDTO>> findByName(@RequestParam(name = "name", defaultValue = "") String name){
        List<WalletResponseDTO> response = walletService.findByName(name);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<WalletResponseDTO> insert(@RequestBody @Valid WalletRequestDTO dto){
        WalletResponseDTO responseDTO = walletService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<WalletResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid WalletRequestDTO dto){
        return ResponseEntity.ok(walletService.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        walletService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
