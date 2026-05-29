package com.music.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload.dir:./uploads}")
    private String uploadDir;

    @Value("${file.upload.audio.max-size:104857600}")
    private long audioMaxSize;

    @Value("${file.upload.audio.allowed-types:mp3,wav,flac,aac,ogg,wma,m4a}")
    private String audioAllowedTypes;

    @Value("${file.upload.cover.max-size:10485760}")
    private long coverMaxSize;

    @Value("${file.upload.cover.allowed-types:jpg,jpeg,png,gif,webp}")
    private String coverAllowedTypes;

    public String storeAudioFile(MultipartFile file) {
        validateFile(file, audioMaxSize, audioAllowedTypes, "audio");
        String subDir = "audio";
        return storeFile(file, subDir);
    }

    public String storeCoverFile(MultipartFile file) {
        validateFile(file, coverMaxSize, coverAllowedTypes, "cover");
        String subDir = "covers";
        return storeFile(file, subDir);
    }

    private void validateFile(MultipartFile file, long maxSize, String allowedTypes, String fileType) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        if (file.getSize() > maxSize) {
            throw new RuntimeException(fileType + " file size exceeds limit");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new RuntimeException("Invalid file name");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedList = Arrays.asList(allowedTypes.split(","));
        if (!allowedList.contains(extension)) {
            throw new RuntimeException("Unsupported " + fileType + " format: " + extension);
        }
    }

    private String storeFile(MultipartFile file, String subDir) {
        Path uploadPath = Paths.get(uploadDir, subDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory: " + e.getMessage());
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;

        Path targetLocation = uploadPath.resolve(newFilename);
        try {
            file.transferTo(targetLocation.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }

        return "/uploads/" + subDir + "/" + newFilename;
    }

    public void deleteFile(String filePath) {
        if (filePath != null && filePath.startsWith("/uploads/")) {
            try {
                Path path = Paths.get(uploadDir, filePath.substring("/uploads/".length())).toAbsolutePath().normalize();
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file: " + e.getMessage());
            }
        }
    }
}
