package com.moneyflow.dto;

public record OfxImportResultDTO(
        int inserted,
        int duplicated,
        int inValidation,
        int errors,
        String message
) {
    public static OfxImportResultDTO of(int inserted, int duplicated, int inValidation, int errors){
        return new OfxImportResultDTO(
                inserted, duplicated, inValidation, errors,
                "Importação concluída: %d inseridas, %d duplicadas, %d em validação, %d erros"
                        .formatted(inserted, duplicated, inValidation, errors)
        );
    }
}
