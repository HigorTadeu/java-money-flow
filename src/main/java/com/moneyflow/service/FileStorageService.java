package com.moneyflow.service;

import com.moneyflow.config.file.FileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);
    private final FileConfig fileConfig;

    public FileStorageService(FileConfig fileConfig){
        this.fileConfig = fileConfig;
    }

    public Path saveTempFile(MultipartFile file) throws IOException {
        validateFile(file);

        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destiny = FileConfig.getTempDir().resolve(uniqueName);
        file.transferTo(destiny);
        log.debug("Arquivo OFX salvo temporariamente em: {}",destiny);
        return destiny;
    }

    private void validateFile(MultipartFile file) {
        long maxByes = fileConfig.getMaxFileSizeKilobyte() * 1024L;
        if (file.getSize() > maxByes){
            throw new IllegalArgumentException(
                    "Arquivo excede o tamanho máximo permitido de %d KB."
                            .formatted(fileConfig.getMaxFileSizeKilobyte()));
        }
    }

    public void delete(Path path){
        try {
            Files.deleteIfExists(path);
            log.debug("Arquivo temporário deletado: {}", path);
        } catch (IOException e) {
            log.warn("Não foi possível deletar arquivo temporário: {}", path, e);
        }
    }
}
