package com.moneyflow.service;

import com.moneyflow.dto.OfxTransactionDTO;
import com.moneyflow.entity.enuns.OfxFormato;
import com.moneyflow.mappers.OfxMapper;
import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;
import com.webcohesion.ofx4j.io.nanoxml.NanoXMLOFXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OfxService {

    private static final Logger log = LoggerFactory.getLogger(OfxService.class);
    private final OfxMapper ofxMapper;

    public OfxService(OfxMapper ofxMapper){
        this.ofxMapper = ofxMapper;
    }

    public List<OfxTransactionDTO> parsear(MultipartFile file) throws IOException {
       try (InputStream is = file.getInputStream()) {
           return parsearStream(is);
       }
    }

    public List<OfxTransactionDTO> parsear(Path path) throws IOException {
        try(InputStream is = Files.newInputStream(path)){
            return parsearStream(is);
        }
    }

    private List<OfxTransactionDTO> parsearStream(InputStream is) throws IOException {
        try{
            byte[] bytes = is.readAllBytes();

            //1. Detacta o formato antes de qualquer processamento
            OfxFormato formato = detectaFormato(bytes);
            log.info("Formato OFX detectado: {}", formato);

            //2. Normaliza datas curtas independente do formato
            Charset charset = resolverCharset(bytes, formato);
            String conteudo = new String(bytes, charset);
            String normalizado = normalizarDatas(conteudo);
            log.debug("Conteúdo OFX normalizado (primeiros 600 chars):\n{}",
                    normalizado.substring(0, Math.min(600, normalizado.length())));

            byte[] bytesNormalizados = normalizado.getBytes(charset);

            // 3. Faz o parse com o reader correto para o formato detectado
            //AggregateUnmarshaller<ResponseEnvelope> unmarshaller = new AggregateUnmarshaller<>(ResponseEnvelope.class);
            AggregateUnmarshaller<ResponseEnvelope> unmarshaller = new AggregateUnmarshaller<>(ResponseEnvelope.class);

            ResponseEnvelope envelope = unmarshaller.unmarshal(new ByteArrayInputStream(bytesNormalizados));

            BankingResponseMessageSet messages =
                    (BankingResponseMessageSet) envelope.getMessageSet(MessageSetType.banking);

            if(messages == null || messages.getStatementResponses().isEmpty()){
                throw new IllegalArgumentException("Arquivo OFX não contém extrato bancário");
            }

            BankStatementResponse extrato = messages.getStatementResponses().get(0).getMessage();

            List<OfxTransactionDTO> transacoes = extrato.getTransactionList()
                    .getTransactions()
                    .stream()
                    .map(ofxMapper::transactionOfxToDTO)
                    .toList();

            log.info("OFX parseado com sucesso: {} transações encontradas ", transacoes.size());

            return transacoes;
        }catch (OFXParseException e) {
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());
            log.error("Erro ao parsear OFX:", e);
            throw new RuntimeException("Erro ao parsear arquivo OFX: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error:" + e.getMessage());
            log.error("Exceção inesperada ao parsear OFX:", e);
            throw new RuntimeException("Erro inesperado ao parsear OFX: " + e.getMessage(), e);
        }
    }

    /**
     * Detecta o formato lendo os primeiros bytes do arquivo.
     * Não faz parse — apenas inspeciona o cabeçalho textual.
     *
     * Indicadores de XML_V2:
     *   - Começa com <?xml
     *   - Começa com <?OFX
     *   - Contém DATA:OFXSGML=NONE ou VERSION:151+ no cabeçalho
     *
     * Indicadores de SGML_V1:
     *   - Contém OFXHEADER:100
     *   - Contém DATA:OFXSGML
     */
    private OfxFormato detectaFormato(byte[] bytes) {
        //Lê apenas os 512 bytes iniciais para deteccao - suficiente para o cabeçalho do arquivo
        int tamanhoPreview = Math.min(bytes.length, 512);
        String preview = new String(bytes, 0, tamanhoPreview, StandardCharsets.ISO_8859_1)
                .trim()
                .toUpperCase();

        //XML V2 - declaração XML ou OFX na primeira linha
        if(preview.startsWith("<?XML") || preview.startsWith("<?OFX")){
            return OfxFormato.XML_V2;
        }

        // XML V2 — versão 151 ou superior no cabeçalho SGML indica formato XML
        if (preview.contains("VERSION:151") ||
                preview.contains("VERSION:160") ||
                preview.contains("VERSION:200") ||
                preview.contains("VERSION:211") ||
                preview.contains("DATA:OFXSGML") && preview.contains("VERSION:2")) {
            return OfxFormato.XML_V2;
        }

        // SGML V1 — cabeçalho padrão dos bancos brasileiros
        if (preview.contains("OFXHEADER:100") || preview.contains("DATA:OFXSGML")) {
            return OfxFormato.SGML_V1;
        }

        // Fallback — tenta SGML V1 que é o mais comum no Brasil
        log.warn("Formato OFX não identificado com certeza, usando SGML_V1 como fallback. " +
                "Preview do cabeçalho: {}", preview.substring(0, Math.min(preview.length(), 100)));
        return OfxFormato.SGML_V1;
    }

    /**
     * Resolve o charset correto para leitura do conteúdo.
     * OFX V1 (SGML) brasileiro usa CHARSET:1252 → ISO-8859-1.
     * OFX V2 (XML) usa UTF-8 por padrão.
     */
    private Charset resolverCharset(byte[] bytes, OfxFormato formato) {
        if(formato == OfxFormato.XML_V2){
            // XML declara o encoding internamente — UTF-8 é o padrão
            String preview = new String(bytes, 0, Math.min(bytes.length, 512), StandardCharsets.UTF_8);
            if (preview.contains("encoding=\"ISO-8859-1\"") ||
                    preview.contains("encoding='ISO-8859-1'")) {
                return StandardCharsets.ISO_8859_1;
            }
            return StandardCharsets.UTF_8;
        }

        // SGML V1 — detecta pelo cabeçalho
        String preview = new String(bytes, 0, Math.min(bytes.length, 512), StandardCharsets.ISO_8859_1);
        if (preview.contains("CHARSET:UTF-8")) {
            return StandardCharsets.UTF_8;
        }
        // CHARSET:1252 e CHARSET:1 ambos mapeiam para ISO-8859-1 no Java
        return Charset.forName("ISO-8859-1");
    }


    /**
     * Normaliza datas no formato YYYYMMDD para YYYYMMDDHHMMSS.
     * A ofx4j falha ao parsear datas curtas em campos do BANKTRANLIST
     * como DTSTART e DTEND, confundindo o mês com valores de enum.
     *
     * Exemplo: 20260201 → 20260201120000
     */
    private String normalizarDatas(String conteudo) {
        // Regex que captura tags de data seguidas de exatamente 8 dígitos
        Pattern pattern = Pattern.compile(
                "(<(?:DTSTART|DTEND|DTPOSTED|DTASOF)>)(\\d{8})(?![\\d\\[])"
        );
        Matcher matcher = pattern.matcher(conteudo);
        StringBuffer sb  = new StringBuffer();
        while (matcher.find()){
            String tag = matcher.group(1);
            String data = matcher.group(2);
            String substituicao = tag + data + "120000";
            log.debug("Normalizando data: {}{} → {}", tag, data, substituicao);
            matcher.appendReplacement(sb, substituicao);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
