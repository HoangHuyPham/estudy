package vn.nlu.huypham.app.service.imp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.constant.ResourceTypes;
import vn.nlu.huypham.app.constant.ResourceVisibilities;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.ResourceRepo;
import vn.nlu.huypham.app.service.StorageService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class StorageServiceImp implements StorageService {
    final AppConfig appConfig;
    final ResourceRepo resourceRepo;
    final Tika tika = new Tika();

    @Override
    public void saveOnDisk(MultipartFile file, Path target) throws AppException {
        try {
            Files.createDirectories(target.getParent());
            try (InputStream is = file.getInputStream()) {
                Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            log.error("Failed to save file on disk", e);
            throw Errors.STORAGE_FAILURE;
        }
    }

    @Override
    public Resource store(MultipartFile file, User user, ResourceVisibilities visibility, ResourceTypes type) throws AppException {
        UUID id = UUID.randomUUID();
        Path xAccelRedirect = Path.of("/")
                .resolve("disk-storage" )
                .resolve(!visibility.equals(ResourceVisibilities.PUBLIC) ? appConfig.getStorage().getProtectedUri() : appConfig.getStorage().getPublicUri())
                .resolve(id.toString())
                .resolve("source" + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        Path diskPath = Path.of(appConfig.getStorage().getRootLocation())
                .resolve(!visibility.equals(ResourceVisibilities.PUBLIC) ? appConfig.getStorage().getProtectedUri() : appConfig.getStorage().getPublicUri())
                .resolve(id.toString())
                .resolve("source" + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        saveOnDisk(file, diskPath);
        Resource resource = Resource.builder()
                .id(id)
                .xAccelRedirect(xAccelRedirect.toString())
                .diskPath(diskPath.toString())
                .size(file.getSize())
                .visibility(visibility)
                .type(type)
                .owner(user)
                .build();
        return resourceRepo.save(resource);
    }

    @Override
    public void validate(MultipartFile file, List<String> expectedTypes, List<String> expectedMimes)
            throws AppException {
        try {
            String contentType = tika.detect(file.getInputStream());
            if (expectedMimes != null && !expectedMimes.isEmpty()) {
                if (!expectedMimes.contains(file.getContentType())) {
                    throw Errors.INVALID_MIME_TYPE;
                }
            }
            if (expectedTypes != null && !expectedTypes.isEmpty()) {
                boolean isValid = false;
                for (String expectedType : expectedTypes) {
                    if (contentType.startsWith(expectedType + "/")) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    throw Errors.INVALID_FILE_TYPE;
                }
            }
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw Errors.STORAGE_FAILURE;
        }
    }
}
