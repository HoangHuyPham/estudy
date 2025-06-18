package vn.edu.hcmuaf.be.controller.global;

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
}
