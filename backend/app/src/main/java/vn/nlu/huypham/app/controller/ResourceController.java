package vn.nlu.huypham.app.controller;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
@Slf4j
public class ResourceController {
    final ResourceService resourceService;
    final ModelMapper modelMapper;

    @GetMapping("/{resourceId}")
    public ResponseEntity<?> serve(@PathVariable String resourceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
        if (authentication instanceof AnonymousAuthenticationToken) {
            Resource resource = resourceService.canAccess(UUID.fromString(resourceId), null);
            return ResponseEntity.ok()
                    .header("X-Accel-Redirect", resource.getDiskURI())
                    .body(null);
        } else {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Resource resource = resourceService.canAccess(UUID.fromString(resourceId), userPrincipal.getUser().getId());
            return ResponseEntity.ok()
                    .header("X-Accel-Redirect", resource.getDiskURI())
                    .body(null);
        }
    }
}
