package vn.nlu.huypham.app.service.imp;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.constant.ResourceVisibilities;
import vn.nlu.huypham.app.constant.Roles;
import vn.nlu.huypham.app.entity.Enrollment;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.entity.embeded_id.EnrollmentId;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.EnrollmentRepo;
import vn.nlu.huypham.app.repository.ResourceRepo;
import vn.nlu.huypham.app.repository.UserRepo;
import vn.nlu.huypham.app.service.ResourceService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class ResourceServiceImp implements ResourceService {

    final EnrollmentRepo enrollmentRepo;
    final ResourceRepo resourceRepo;
    final UserRepo userRepo;

    @Override
    public Resource canAccess(UUID resourceId, UUID userId) throws AppException {

        Resource resource = resourceRepo.findById(resourceId).orElseThrow(() -> {
            log.warn("Resource {} not found", resourceId);
            return Errors.RESOURCE_NOT_FOUND;
        });

        // Pass with public resource
        if (resource.getVisibility().equals(ResourceVisibilities.PUBLIC))
            return resource;

        User user = userRepo.findById(userId).orElseThrow(() -> Errors.RESOURCE_CAN_NOT_ACCESS);

        // Pass with admin role
        if (user.getRoles().stream().anyMatch((u) -> u.getName().equals(Roles.ADMIN)))
            return resource;

        // Pass with owner
        if (resource.getOwner().getId().equals(userId))
            return resource;

        // Check with protected resource
        if (resource.getVisibility().equals(ResourceVisibilities.PROTECTED)) {
            if (resource.getCourse() != null) {
                Enrollment enrollment = enrollmentRepo.findById(EnrollmentId.builder()
                        .courseId(resource.getCourse().getId())
                        .userId(userId)
                        .build()).orElseThrow(() -> Errors.RESOURCE_CAN_NOT_ACCESS);
                if (enrollment.getUser().getId().equals(userId))
                    return resource;
            }
        }

        throw Errors.RESOURCE_CAN_NOT_ACCESS;
    }
}
