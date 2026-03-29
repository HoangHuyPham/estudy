package vn.nlu.huypham.app.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.dto.request.LoginBasic;
import vn.nlu.huypham.app.dto.request.LoginGoogle;
import vn.nlu.huypham.app.dto.request.RegisterBasic;
import vn.nlu.huypham.app.dto.request.RegisterOTPBasic;
import vn.nlu.huypham.app.dto.response.ATAndRT;
import vn.nlu.huypham.app.payload.ApiResponse;
import vn.nlu.huypham.app.service.AuthService;
import vn.nlu.huypham.app.service.JWTService;

import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

        final AuthService authService;
        final JWTService jwtService;
        final AppConfig appConfig;

        @PostMapping("/login")
        public ResponseEntity<?> login(@Valid @RequestBody LoginBasic loginBasic) throws Exception {

                ATAndRT result = authService.auth(loginBasic.getUsername(), loginBasic.getPassword());

                ResponseCookie cookie = ResponseCookie.from("refreshToken", result.getRefreshToken().toString())
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(appConfig.getJwt().getRefreshExp())
                                .sameSite("Lax")
                                .build();

                return ResponseEntity.ok()
                                .header("Set-Cookie", cookie.toString())
                                .body(ApiResponse.builder()
                                                .code(200)
                                                .message("Login successful")
                                                .data(result.getAccessToken())
                                                .build());
        }

        @PostMapping("/pre-register")
        public ResponseEntity<?> preRegister(@Valid @RequestBody RegisterOTPBasic registerOTPBasic) throws Exception {
                UUID result = authService.preRegister(registerOTPBasic);

                return ResponseEntity.ok()
                                .body(ApiResponse.builder()
                                                .code(200)
                                                .message("Pre-register successful")
                                                .data(result)
                                                .build());
        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody RegisterBasic registerBasic) throws Exception {
                ATAndRT result = authService.register(registerBasic, registerBasic.getOtp());

                ResponseCookie cookie = ResponseCookie.from("refreshToken",
                                result.getRefreshToken().toString())
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(appConfig.getJwt().getRefreshExp())
                                .sameSite("Lax")
                                .build();

                return ResponseEntity.ok()
                                .header("Set-Cookie", cookie.toString())
                                .body(ApiResponse.builder()
                                                .code(200)
                                                .message("Register successful")
                                                .data(result.getAccessToken())
                                                .build());
        }

        @PostMapping("/login/google")
        public ResponseEntity<?> login(@RequestBody LoginGoogle loginGoogle) throws Exception {
                ATAndRT result = authService.auth(loginGoogle.getToken());

                ResponseCookie cookie = ResponseCookie.from("refreshToken",
                                result.getRefreshToken().toString())
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(appConfig.getJwt().getRefreshExp())
                                .sameSite("Lax")
                                .build();

                return ResponseEntity.ok()
                                .header("Set-Cookie", cookie.toString())
                                .body(ApiResponse.builder()
                                                .code(200)
                                                .message("Login successful")
                                                .data(result.getAccessToken())
                                                .build());
        }

        @GetMapping("/refresh-token")
        public ResponseEntity<?> rotateRefreshToken(
                        @Parameter(hidden = true) @CookieValue(name = "refreshToken") String token) {

                ATAndRT result = jwtService.rotateRT(token);
                String accessToken = result.getAccessToken();
                String refreshToken = result.getRefreshToken().toString();

                ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                                .httpOnly(true)
                                .secure(false)
                                .path("/")
                                .maxAge(appConfig.getJwt().getRefreshExp())
                                .sameSite("Lax")
                                .build();

                return ResponseEntity.ok()
                                .header("Set-Cookie", cookie.toString())
                                .body(ApiResponse.builder()
                                                .code(200)
                                                .message("Rotate refresh token successful")
                                                .data(accessToken)
                                                .build());
        }
}
