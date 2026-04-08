package com.moneyflow.entity.enuns;

/**
 * Formatos possíveis de arquivo OFX.
 *
 * SGML_V1 — OFX versão 1.x: cabeçalho em texto puro (OFXHEADER:100, DATA:OFXSGML)
 *           seguido de tags sem fechamento. Padrão dos bancos brasileiros.
 *
 * XML_V2  — OFX versão 2.x: arquivo XML puro com declaração <?xml ...?>
 *           ou <?OFX ...?>. Padrão de bancos internacionais e alguns brasileiros.
 */
public enum OfxFormato {
    SGML_V1,
    XML_V2
}
