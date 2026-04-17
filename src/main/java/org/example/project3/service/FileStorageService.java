package org.example.project3.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads2/";

    /**
     * Handles file saving logic and returns the relative path for DB storage.
     */
    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) return null;

        Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return "/" + UPLOAD_DIR + file.getOriginalFilename();
    }

    public String getFileName(MultipartFile file) {
        return file.getOriginalFilename();
    }
}