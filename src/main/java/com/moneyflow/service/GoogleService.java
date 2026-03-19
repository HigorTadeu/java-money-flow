package com.moneyflow.service;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.moneyflow.config.google.GoogleConnection;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GoogleService {

    private GoogleConnection googleConnection;

    public GoogleService(GoogleConnection googleConnection){
        this.googleConnection = googleConnection;
    }

    /**
     * Método responsável pela leitura dos dados do Sheet
     * @param spreadSheetId Id do Sheet
     * @param range Internvalo que será lido o Sheet
     * @return Retorna ao informações do Sheet
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public List<List<Object>> readSheet(String spreadSheetId, String range) throws GeneralSecurityException, IOException {
        ValueRange response = googleConnection.getSheetsService()
                .spreadsheets().values()
                .get(spreadSheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        return values != null ? values : List.of();
    }

    /**
     * Método filtra os dados do Sheet enviado com base no período que foi solicitado
     * @param sheet Informação do Sheet
     * @param beginDate Data de Início da Período
     * @param endDate Data Fim do Período
     * @return Um Sheet filtrado com base nos parâmetros passados
     */
    public List<List<Object>> filterDatePeriod(List<List<Object>> sheet, LocalDate beginDate, LocalDate endDate){
        if (sheet != null && !sheet.isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return sheet.stream()
                    .filter((row -> {
                        if(row.size() < 2 || row.get(2) == null || row.get(2).toString().isEmpty()) return false;
                        try{
                            LocalDate rowDate = LocalDate.parse(row.get(2).toString(), formatter);
                            return !rowDate.isBefore(beginDate) && !rowDate.isAfter(endDate);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })).toList();
        }
        return null;
    }

}
