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
public class GoogleSheetService {
    private final GoogleConnection googleConnection;

    public GoogleSheetService(GoogleConnection googleConnection){
        this.googleConnection = googleConnection;
    }

    public List<List<Object>> readSheet(String spreadsheetId, String range) throws GeneralSecurityException, IOException {
        ValueRange response = googleConnection.getSheetsService()
                .spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        return values != null ? values : List.of();
    }

    public void appendRows(String spreadsheetId, String sheet, List<List<Object>> rows) throws GeneralSecurityException, IOException {
        if(rows == null || rows.isEmpty()) return;
        ValueRange body = new ValueRange().setValues(rows);
        googleConnection.getSheetsService()
                .spreadsheets().values()
                .append(spreadsheetId, sheet + "!A1", body)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    public List<List<Object>> filterDatePeriod(List<List<Object>> sheet, LocalDate beginDate, LocalDate endDate){
        if(sheet == null || sheet.isEmpty()) return List.of();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return sheet.stream()
                .filter(row -> {
                    if(row.size() < 3 || row.get(2) == null || row.get(2).toString().isBlank()) return false;
                    try{
                        LocalDate rowDate = LocalDate.parse(row.get(2).toString(), formater);
                        return !rowDate.isBefore(beginDate) && !rowDate.isAfter(endDate);
                    }catch (Exception e){
                        return false;
                    }
                })
                .toList();
    }
}
