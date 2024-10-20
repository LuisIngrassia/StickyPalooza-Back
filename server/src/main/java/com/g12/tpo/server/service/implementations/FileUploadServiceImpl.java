package com.g12.tpo.server.service.implementations;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.g12.tpo.server.service.interfaces.FileUploadService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads"; 

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Cannot upload empty file.");
        }

        String originalFilename = file.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "_" + originalFilename;

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get(UPLOAD_DIR, newFileName);
        Files.copy(file.getInputStream(), filePath);

        return "/uploads/" + newFileName; 
    }
}
