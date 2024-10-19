package com.g12.tpo.server.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadImage(MultipartFile file) throws IOException;
}
