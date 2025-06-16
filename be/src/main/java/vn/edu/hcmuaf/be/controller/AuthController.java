package vn.edu.hcmuaf.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.edu.hcmuaf.be.dto.ApiResponse;
import vn.edu.hcmuaf.be.dto.login.LoginRequestDTO;
import vn.edu.hcmuaf.be.dto.login.LoginResponseDTO;
import vn.edu.hcmuaf.be.dto.register.RegisterRequestDTO;
import vn.edu.hcmuaf.be.entity.User;
import vn.edu.hcmuaf.be.jwt.JwtUtil;
import vn.edu.hcmuaf.be.repository.UserRepository;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "login" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequestDTO dto){
       
        User existUser = userRepository.findByusername(dto.getUsername());
        if (existUser != null && BCrypt.checkpw(dto.getPassword(), existUser.getPassword())){
            return new ResponseEntity<ApiResponse<?>>(
                ApiResponse.builder()
                .message("success")
                .data(new LoginResponseDTO(jwtUtil.generateToken(existUser)))
                .build()
            , HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequestDTO dto) {

        if (userRepository.findByusername(dto.getUsername()) != null) {
            return new ResponseEntity<>(
                    ApiResponse.builder()
                            .message("Username đã tồn tại")
                            .data(null)
                            .build(),
                    HttpStatus.CONFLICT
            );
        }

        String hashed = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(hashed);
        userRepository.save(u);

        String token = jwtUtil.generateToken(u);

        return new ResponseEntity<>(
                ApiResponse.builder()
                        .message("success")
                        .data(new LoginResponseDTO(token))
                        .build(),
                HttpStatus.OK
        );
    }
}
