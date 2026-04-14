package vn.nlu.huypham.app.service;

import java.nio.file.Path;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface StorageService {
    Resource store(MultipartFile file, User user, boolean isProtected) throws AppException;
    void validate(MultipartFile file, List<String> expectedTypes, List<String> expectedMimes) throws AppException;
    void saveOnDisk(MultipartFile file, Path target) throws AppException;
}
