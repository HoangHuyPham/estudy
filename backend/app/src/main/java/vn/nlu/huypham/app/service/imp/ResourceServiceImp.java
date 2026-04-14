package vn.nlu.huypham.app.service.imp;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.ResourceRepo;
import vn.nlu.huypham.app.service.ResourceService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class ResourceServiceImp implements ResourceService {

    final ResourceRepo resourceRepo;

    @Override
    public Resource canAccess(UUID resourceId, UUID userId) throws AppException{

        Resource resource = resourceRepo.findById(resourceId).orElseThrow(() -> {
            log.warn("Resource {} not found", resourceId);
            return Errors.RESOURCE_NOT_FOUND;
        });

        // visibility check
        if (!resource.isProtected())
            return resource;

        // owner check
        if (userId != null && resource.getUser().getId().equals(userId))
            return resource;

        throw Errors.RESOURCE_CAN_NOT_ACCESS;
    }
}
