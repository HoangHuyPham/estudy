package vn.nlu.huypham.app.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.ResourceTypes;
import vn.nlu.huypham.app.constant.ResourceVisibilities;
import vn.nlu.huypham.app.dto.response.FileInfo;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.payload.ApiResponse;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.StorageService;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@Slf4j
public class StorageController {
    final StorageService storageService;
    final ModelMapper modelMapper;

    @PostMapping(value = "/video", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> storeVideo(@RequestPart("file") MultipartFile file) throws Exception {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        storageService.validate(file, List.of("video"), List.of("video/mp4"));
        Resource resource = storageService.store(file, principal.getUser(), ResourceVisibilities.PROTECTED, ResourceTypes.VIDEO);
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .code(200)
                        .message("Video uploaded successfully")
                        .data(modelMapper.map(resource, FileInfo.class))
                        .build());
    }

    @PostMapping(value = "/image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> storeImage(@RequestPart("file") MultipartFile file) throws Exception {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        storageService.validate(file, List.of("image"), List.of("image/jpeg", "image/png"));
        Resource resource = storageService.store(file, principal.getUser(), ResourceVisibilities.PUBLIC, ResourceTypes.IMAGE);

        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .code(200)
                        .message("Image uploaded successfully")
                        .data(modelMapper.map(resource, FileInfo.class))
                        .build());
    }
}
