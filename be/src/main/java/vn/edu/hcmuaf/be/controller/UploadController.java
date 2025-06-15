package vn.edu.hcmuaf.be.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.edu.hcmuaf.be.config.CustomConfig;
import vn.edu.hcmuaf.be.dto.ApiResponse;
import vn.edu.hcmuaf.be.entity.Image;
import vn.edu.hcmuaf.be.entity.Video;
import vn.edu.hcmuaf.be.repository.ImageRepository;
import vn.edu.hcmuaf.be.repository.VideoRepository;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private CustomConfig config;
    @Autowired
    private ImageRepository repositoryImage;
    @Autowired
    private VideoRepository repositoryVideo;

    @PostMapping(value = "image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(fileName);
        String fileNameHash = UUID.randomUUID().toString() + "." + extension;
        String url = "http://localhost:8080/public/" + fileNameHash;
        Path uploadDir = Path.of(config.getUpload().getPath());

        if (!Files.isDirectory(uploadDir)) {
            Files.createDirectory(uploadDir);
        }

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            if (!List.of("jpg", "jpeg", "png").contains(extension)) {
                return new ResponseEntity<>(ApiResponse.builder()
                        .message("Invalid file type. Only JPG, JPEG, PNG are allowed.")
                        .data(null)
                        .build(), HttpStatus.BAD_REQUEST);
            }

            Files.copy(file.getInputStream(), Path.of(config.getUpload().getPath(), fileNameHash),
                    StandardCopyOption.REPLACE_EXISTING);

            Image newImage = Image.builder().name(fileName).url(url).build();
            repositoryImage.save(newImage);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
            return new ResponseEntity<>(ApiResponse.builder()
                    .message("error")
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ApiResponse.builder()
                .message("success")
                .data(url)
                .build(), HttpStatus.OK);
    }

    @PostMapping(value = "video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(fileName);
        String fileNameHash = UUID.randomUUID().toString() + "." + extension;
        String url = "http://localhost:8080/public/" + fileNameHash;
        Path uploadDir = Path.of(config.getUpload().getPath(), fileNameHash);

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        Path tempPath = Files.createTempFile("upload-", null);
        File tempFile = tempPath.toFile();
        file.transferTo(tempFile);

        try {
            if (!List.of("mp4").contains(extension)) {
                return new ResponseEntity<>(ApiResponse.builder()
                        .message("Invalid file type. Only MP4 are allowed.")
                        .data(null)
                        .build(), HttpStatus.BAD_REQUEST);
            }

            Metadata metadata = new Metadata();
            AutoDetectParser parser = new AutoDetectParser();
            try (InputStream stream = Files.newInputStream(tempPath)) {
                parser.parse(stream, new BodyContentHandler(), metadata, new ParseContext());
            }

            double durationDouble = Double.parseDouble(metadata.get("xmpDM:duration"));
            long durationInSeconds = (long)(durationDouble / 1);

            Files.copy(tempPath, uploadDir, StandardCopyOption.REPLACE_EXISTING);

            Video newVideo = Video.builder().name(fileName).url(url).duration(durationInSeconds).build();
            repositoryVideo.save(newVideo);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            e.printStackTrace();
            return new ResponseEntity<>(ApiResponse.builder()
                    .message("error")
                    .data(null)
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            Files.deleteIfExists(tempPath);
            System.out.println("Removed temp file: %s".formatted(tempPath));
        }

        return new ResponseEntity<>(ApiResponse.builder()
                .message("success")
                .data(url)
                .build(), HttpStatus.OK);
    }

    public String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1)
                ? fileName.substring(lastDotIndex + 1).toLowerCase()
                : "";
    }
}
