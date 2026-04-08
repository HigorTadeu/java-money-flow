package com.moneyflow.config.file;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.MultipartConfigElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileConfig {
    private static final Logger log = LoggerFactory.getLogger(FileConfig.class);

    private static final Path TEMP_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "moneyflow","ofx");

    private final long maxFileSizeKilobyte;

    public FileConfig(@Value("${ofx.file.max.size.kilobyte}") long maxFileSizeKilobyte) {
        this.maxFileSizeKilobyte = maxFileSizeKilobyte;
    }

    @PostConstruct
    public void inicializar(){
        try{
            Files.createDirectories(TEMP_DIR);
            log.info("Diretório temporário OFX inicializado: {}", TEMP_DIR);
            log.info("Tamanho máximo de upload OFX: {} KB", maxFileSizeKilobyte);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar o diretório temporário OFX: " + TEMP_DIR);
        }
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofKilobytes(maxFileSizeKilobyte));
        factory.setFileSizeThreshold(DataSize.ofKilobytes(maxFileSizeKilobyte));
        return factory.createMultipartConfig();
    }

    public static Path getTempDir(){
        return TEMP_DIR;
    }

    public long getMaxFileSizeKilobyte(){
        return maxFileSizeKilobyte;
    }
}