package vn.edu.hcmuaf.be.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.edu.hcmuaf.be.dto.ApiResponse;
import vn.edu.hcmuaf.be.dto.user.UserInfoDTO;
import vn.edu.hcmuaf.be.entity.User;
import vn.edu.hcmuaf.be.repository.UserRepository;

@RestController
@RequestMapping("/api/self-user-info")
public class SelfUserInfoController {
        @Autowired
        private UserRepository repoUser;

        @Autowired
        private ModelMapper modelMapper;

        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ApiResponse<?>> getSelf() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();
                User existUser = repoUser.findById(user.getId()).orElse(null);

                if (existUser == null) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder()
                                                        .data(null)
                                                        .message("success")
                                                        .build(),
                                        HttpStatus.NO_CONTENT);
                }

                UserInfoDTO userDTO = modelMapper.map(existUser, UserInfoDTO.class);

                return new ResponseEntity<>(
                                ApiResponse.builder()
                                                .data(userDTO)
                                                .message("success")
                                                .build(),
                                HttpStatus.OK);
        }
}
