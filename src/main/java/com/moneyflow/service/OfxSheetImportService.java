package com.moneyflow.service;

import com.moneyflow.dto.OfxImportResultDTO;
import com.moneyflow.dto.OfxTransactionDTO;
import com.moneyflow.entity.enuns.OfxType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.DecimalStyle;
import java.util.*;

@Service
public class OfxSheetImportService {
    private static  final Logger log = LoggerFactory.getLogger(OfxSheetImportService.class);

    private static final int COL_DATA = 2;
    private static final int COL_VALOR = 6;
    private static final int COL_FITID = 16;

    private static final DateTimeFormatter BR_FORMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Value("${SHEET_TRANSACTION_EXPENSE}")
    private String sheetTransactionExpense;
    @Value("${SHEET_VALIDATION}")
    private String sheetValidation;
    @Value("${SHEET_IMPORT}")
    private String spreadsheetId;


    private final OfxService ofxService;
    private final GoogleSheetService googleSheetService;
    private final FileStorageService fileStorageService;

    public OfxSheetImportService(OfxService ofxService, GoogleSheetService googleSheetService, FileStorageService fileStorageService){
        this.ofxService = ofxService;
        this.googleSheetService = googleSheetService;
        this.fileStorageService = fileStorageService;
    }

    public OfxImportResultDTO importOfx(MultipartFile file, String bank) throws IOException, GeneralSecurityException{
        Path tempFile = null;
        try{
            //1. Salva temporariamente no disco
            tempFile = fileStorageService.saveTempFile(file);

            //2. Parseia do disco
            List<OfxTransactionDTO> transactions = ofxService.parsear(tempFile);

            //3. Carrega planilha para única chamada da API
            List<List<Object>> dataSheet = googleSheetService.readSheet(spreadsheetId,sheetTransactionExpense + "!A2:R");

            //4. Indexa para buscas eficientes
            Set<String> fitIdsExisted = indexarFitIds(dataSheet);
            Map<String, List<Integer>> indiceDataValor = indexarPorDataValor(dataSheet);

            List<List<Object>> linhasInserir = new ArrayList<>();
            List<List<Object>> linhasValidacao = new ArrayList<>();
            int duplicadas = 0;

            // 5. Classifica cada transação OFX
            for (OfxTransactionDTO tx: transactions){
                if(tx.transactionType() == OfxType.DEBIT){
                    if(fitIdsExisted.contains(tx.fitId())){
                        duplicadas++;
                        continue;
                    }

                    String chave = chaveDataValor(tx.transactionDate(), tx.transactionAmount());
                    List<Integer> conflitos = indiceDataValor.getOrDefault(chave,List.of());

                    if(!conflitos.isEmpty()){
                        linhasValidacao.add(montarLinhaValidacao(tx,bank,conflitos));
                        continue;
                    }

                    linhasInserir.add(montarLinhaInsercao(tx,bank));
                }
            }

            // 6. Persiste em batch - uma chamada para aba
            googleSheetService.appendRows(spreadsheetId, sheetTransactionExpense, linhasInserir);
            googleSheetService.appendRows(spreadsheetId, sheetValidation, linhasValidacao);

            log.info("Importação concluída - banco: {}, inseridas: {}, duplicadas: {}, validação: {}",
                    bank, linhasInserir.size(), duplicadas, linhasValidacao.size());

            return OfxImportResultDTO.of(
                    linhasInserir.size(), duplicadas, linhasValidacao.size(), 0
            );

        } finally {
            //7. Garante deleção mesmo se ocorrer exceção
            if(tempFile != null){
                fileStorageService.delete(tempFile);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Indexação
    // -------------------------------------------------------------------------

    private Set<String> indexarFitIds(List<List<Object>> dados){
        Set<String> fitIds = new HashSet<>();
        for(List<Object> linha : dados){
            if(linha.size() > COL_FITID){
                String val = Objects.toString(linha.get(COL_FITID), "").trim();
                if(!val.isBlank()) fitIds.add(val);
            }
        }
        return fitIds;
    }

    private Map<String, List<Integer>> indexarPorDataValor(List<List<Object>> dados){
        Map<String, List<Integer>> indice = new HashMap<>();
        for(int i = 0; i < dados.size(); i++){
            List<Object> linha = dados.get(i);

            if(linha.size() <= Math.max(COL_DATA, COL_VALOR)) continue;

            try{
                LocalDate data = LocalDate.parse(
                        Objects.toString(linha.get(COL_DATA), "").trim(), BR_FORMT
                );
                BigDecimal valor = new BigDecimal(
                        Objects.toString(linha.get(COL_VALOR), "").trim()
                                .replace("R$","").replace(" ","")
                                .replace(".","").replace(",",".")
                );
                indice.computeIfAbsent(chaveDataValor(data,valor),
                        k -> new ArrayList<>()).add(i + 2);
            } catch (DateTimeParseException | NumberFormatException e){
                log.warn("Linha {} ignorada na indexação: {]", i + 2, e.getMessage());
            }
        }
        return indice;
    }

    private String chaveDataValor(LocalDate data, BigDecimal valor){
        return data + "|" + valor.abs().stripTrailingZeros().toPlainString();
    }

    // -------------------------------------------------------------------------
    // Montagem das linhas
    // -------------------------------------------------------------------------

    private List<Object> montarLinhaInsercao(OfxTransactionDTO tx, String banco){
        List<Object> linha = new ArrayList<>(Collections.nCopies(18, ""));
        linha.set(0, tx.transactionDate().getYear());
        linha.set(1, retornarMesFormatado(tx.transactionDate().getMonthValue()));
        linha.set(COL_DATA, tx.transactionDate().format(BR_FORMT));
        linha.set(3,tx.memo());
        linha.set(7,validarFormaPagtoTransacao(tx.memo(), tx.transactionType()));
        linha.set(9, banco);
        linha.set(COL_VALOR,normalizaValorAmount(tx.transactionAmount()));
        linha.set(11, "Sim");
        linha.set(15, "Sim");
        linha.set(COL_FITID, tx.fitId());

        return linha;
    }

    private List<Object> montarLinhaValidacao(OfxTransactionDTO tx, String banco, List<Integer> conflitos){
        String linhas = conflitos.stream()
                .map(String::valueOf)
                .reduce((a,b) -> a + "-" + b)
                .orElse("");
        return List.of(
                banco,
                tx.fitId(),
                tx.transactionDate().format(BR_FORMT),
                tx.memo(),
                normalizaValorAmount(tx.transactionAmount()),
                tx.transactionType().name(),
                linhas
        );
    }

    /**
     * Método para retornar referência do mês para preencher a coluna de mês da planilha
     * @param mes
     * @return Mês formatado
     */
    private String retornarMesFormatado(int mes){
        String month = "";
        switch (mes){
            case 1: return month = "01 Jan";
            case 2: return month = "02 Fev";
            case 3: return month = "03 Mar";
            case 4: return month = "04 Abr";
            case 5: return month = "05 Mai";
            case 6: return month = "06 Jun";
            case 7: return month = "07 Jul";
            case 8: return month = "08 Ago";
            case 9: return month = "09 Set";
            case 10: return month = "10 Out";
            case 11: return month = "11 Nov";
            case 12: return month = "12 Dez";
        }
        return month;
    }

    /**
     * Método para normalizar o valor Amount do OFX
     * 1. Deixar valor positivo sempre positivo independente de ser DEBIT ou CREDIT
     * 2. Formatar para padrão Brasileito de moeada retirando o ponto e colocar a vírgula como casa decimal
     * @param amount Formatado
     * @return
     */
    private String normalizaValorAmount(BigDecimal amount){
        if(amount == null){
            return "0,00";
        }
        //Torna sempre positivo
        BigDecimal positiveAmount = amount.abs();
        return positiveAmount.toPlainString().replace('.',',');
    }

    /**
     * Método para validar retornar a forma de pagamento correta de acordo com o tipo de pagamento e memo
     * Para o banco Caixa Economica o Memo ENVIO PIX referencia o envio de um PIX os demais deverao ser considerados como Débito
     * @param memo Informacao enviada pelo Banco da descrição da transacao
     * @param type Tipo de transação DEBIT ou CREDIT
     * @return
     */
    private String validarFormaPagtoTransacao(String memo, OfxType type){
        String forma = "";
        if(type == OfxType.DEBIT){
            forma = memo.equals("ENVIO PIX") ? "Pix" : "Débito";
        }
        return forma;
    }
}
