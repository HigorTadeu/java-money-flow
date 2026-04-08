package com.moneyflow.entity.enuns;

public enum OfxStatus {
    NEW, // não existe na planilha - pode inserir
    IMPORTED, // FITID já existe na planilha - ignorar
    PENDING_VALIDATION // Existe mesma descricao, mesmo valor, mesma data - precisa analisar
}
