package com.moneyflow.controller;

import com.moneyflow.config.google.GoogleConnection;
import com.moneyflow.dto.*;
import com.moneyflow.service.GoogleSheetService;
import com.moneyflow.service.OfxSheetImportService;
import com.moneyflow.service.TransactionService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.UUID;

@RestController
@RequestMapping("transactions")
public class TransactionController {
    @Value("${SHEET_IMPORT}")
    private String idSheet;

    private TransactionService transactionService;
    private final GoogleConnection googleConnection;
    private final GoogleSheetService googleSheetService;
    private final OfxSheetImportService ofxSheetImportService;

    public TransactionController(GoogleSheetService googleSheetService, GoogleConnection googleConnection, TransactionService transactionService, OfxSheetImportService ofxSheetImportService){
        this.googleSheetService = googleSheetService;
        this.googleConnection = googleConnection;
        this.transactionService = transactionService;
        this.ofxSheetImportService = ofxSheetImportService;
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable UUID id){
        TransactionResponseDTO response = transactionService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/t")
    public ResponseEntity<Page<TransactionResponseDTO>> findByDescription(
            @RequestParam(name="description", defaultValue = "") String description,
            Pageable pageable){
        Page<TransactionResponseDTO> result = transactionService.findByDescription(description, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<TransactionResponseDTO>> findByFilter(
            TransactionFilterDTO filter,
            Pageable pageable
    ){
        Page<TransactionResponseDTO> result = transactionService.findByFilter(filter, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dash")
    public ResponseEntity<TransactionDashResponseDTO> getTransactionInfoDash(TransactionDashFilterDTO dto){
        TransactionDashResponseDTO response = transactionService.getTransactionInfoDash(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> findAll(Pageable pageable){
        Page<TransactionResponseDTO> transactions = transactionService.findAll(pageable);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> insert(@RequestBody TransactionRequestDTO transactionRequestDTO){
        TransactionResponseDTO transactionResponseDTO = transactionService.insert(transactionRequestDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionResponseDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(transactionResponseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody TransactionRequestDTO transactionRequestDTO){
        TransactionResponseDTO transactionResponseDTO = transactionService.update(id, transactionRequestDTO);
        return ResponseEntity.ok(transactionResponseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value="/import/ofx", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OfxImportResultDTO> insertDataOfxFile(
            @RequestParam("arquivo")MultipartFile arquivo,
            @RequestParam("banco") String banco
            ){
        if (arquivo.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        String nome = arquivo.getOriginalFilename();
        if(nome == null || !nome.toLowerCase().endsWith(".ofx")){
            return ResponseEntity.badRequest().build();
        }
        try {
            OfxImportResultDTO resultado = ofxSheetImportService.importOfx(arquivo, banco);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/import")
    public ResponseEntity<Boolean> insertDataOfxFile(){
        String aba = "Transações";
        String range = "A2:D";
        //String range = "Transações!A1:D3";
        transactionService.importDataOfxFile(aba, range);

//        try {
//            List<List<Object>> resultSheet = googleService.readSheet(idSheet,range);
//            if(resultSheet != null && !resultSheet.isEmpty()){
//                int contY = 0;
//                for(int i = 0; i < resultSheet.size(); i++){
//                    contY = resultSheet.get(i).size();
//                    System.out.println("Linha: " + contY);
//                    for(int y = 0; y < contY; y++){
//                        System.out.println(resultSheet.get(i).get(y));
//                    }
//                    //System.out.println(resultado.get(i));
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }
        return ResponseEntity.ok(true);
    }
}
