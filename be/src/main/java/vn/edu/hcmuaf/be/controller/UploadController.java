package vn.edu.hcmuaf.be.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.edu.hcmuaf.be.config.CustomConfig;
import vn.edu.hcmuaf.be.dto.ApiResponse;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private CustomConfig config;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(fileName);
        String fileNameHash = UUID.randomUUID().toString() + "." + extension;
        Path uploadDir = Path.of(config.getUpload().getPath());

        if (!Files.isDirectory(uploadDir)) {
            Files.createDirectory(uploadDir);
        }

        if (!List.of("jpg", "jpeg", "png", "mp4").contains(extension)) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .message("Invalid file type. Only JPG, JPEG, PNG, and MP4 are allowed.")
                .build());
    }


        Files.copy(file.getInputStream(), Path.of(config.getUpload().getPath(), fileNameHash),
                StandardCopyOption.REPLACE_EXISTING);
        return new ResponseEntity<>(ApiResponse.builder()
                .message("success")
                .data("/public/" + fileNameHash)
                .build(), HttpStatus.OK);
    }

    public String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1)
                ? fileName.substring(lastDotIndex + 1).toLowerCase()
                : "";
    }
}
