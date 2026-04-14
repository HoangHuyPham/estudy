package vn.nlu.huypham.app.controller;


import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.dto.response.UserMe;
import vn.nlu.huypham.app.payload.ApiResponse;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    final ModelMapper modelMapper;
    final UserService userService;

    @PostMapping("/me")
    public ResponseEntity<?> me() throws Exception {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok()
                .body(ApiResponse.builder()
                        .code(200)
                        .message("Get data successful")
                        .data(modelMapper.map(principal.getUser(), UserMe.class))
                        .build());
    }
}
