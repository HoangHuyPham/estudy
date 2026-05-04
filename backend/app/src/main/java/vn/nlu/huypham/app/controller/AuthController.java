package vn.nlu.huypham.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.dto.request.PasswordLoginRequest;
import vn.nlu.huypham.app.dto.request.GoogleLoginRequest;
import vn.nlu.huypham.app.dto.request.LogoutRequest;
import vn.nlu.huypham.app.dto.request.RegisterConfirmRequest;
import vn.nlu.huypham.app.dto.request.RegisterRequest;
import vn.nlu.huypham.app.dto.response.TokenPairResponse;
import vn.nlu.huypham.app.payload.ApiResponse;
import vn.nlu.huypham.app.service.AuthService;
import vn.nlu.huypham.app.service.JWTService;

import java.util.UUID;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Misc: Auth")
public class AuthController
{
	final AuthService authService;
	final JWTService jwtService;
	final AppConfig appConfig;

	@PostMapping("/login/basic")
	public ResponseEntity<?> login(
		@Valid @RequestBody PasswordLoginRequest loginBasic) throws Exception
	{

		TokenPairResponse result = authService.auth(loginBasic.getUsername(),
				loginBasic.getPassword());

		ResponseCookie cookie = ResponseCookie
				.from("refreshToken", result.getRefreshToken().toString()).httpOnly(true)
				.secure(true).path("/").maxAge(appConfig.getJwt().getRefreshExp()).sameSite("None")
				.build();

		return ResponseEntity.ok().header("Set-Cookie", cookie.toString())
				.body(ApiResponse.builder().code(200).message("Login successful")
						.data(result.getAccessToken()).build());
	}

	@PostMapping("/pre-register")
	public ResponseEntity<?> preRegister(
		@Valid @RequestBody RegisterRequest registerOTPBasic) throws Exception
	{
		UUID result = authService.preRegister(registerOTPBasic);

		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Pre-register successful").data(result).build());
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(
		@Valid @RequestBody RegisterConfirmRequest registerBasic) throws Exception
	{
		TokenPairResponse result = authService.register(registerBasic, registerBasic.getOtp());

		ResponseCookie cookie = ResponseCookie
				.from("refreshToken", result.getRefreshToken().toString()).httpOnly(true)
				.secure(true).path("/").maxAge(appConfig.getJwt().getRefreshExp()).sameSite("None")
				.build();

		return ResponseEntity.ok().header("Set-Cookie", cookie.toString())
				.body(ApiResponse.builder().code(200).message("Register successful")
						.data(result.getAccessToken()).build());
	}

	@PostMapping("/login/google")
	public ResponseEntity<?> login(
		@RequestBody GoogleLoginRequest loginGoogle) throws Exception
	{
		TokenPairResponse result = authService.auth(loginGoogle.getToken());

		ResponseCookie cookie = ResponseCookie
				.from("refreshToken", result.getRefreshToken().toString()).httpOnly(true)
				.secure(true).path("/").maxAge(appConfig.getJwt().getRefreshExp()).sameSite("None")
				.build();

		return ResponseEntity.ok().header("Set-Cookie", cookie.toString())
				.body(ApiResponse.builder().code(200).message("Login successful")
						.data(result.getAccessToken()).build());
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> rotateRefreshToken(
		@Parameter(hidden = true) @CookieValue(name = "refreshToken") String token)
	{

		TokenPairResponse result = jwtService.rotateRT(token);
		String accessToken = result.getAccessToken();
		String refreshToken = result.getRefreshToken().toString();

		ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true)
				.secure(true).path("/").maxAge(appConfig.getJwt().getRefreshExp()).sameSite("None")
				.build();

		return ResponseEntity.ok().header("Set-Cookie", cookie.toString())
				.body(ApiResponse.builder().code(200).message("Rotate refresh token successful")
						.data(accessToken).build());
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(
		@Parameter(hidden = true) @CookieValue(name = "refreshToken") String token,
		@RequestBody LogoutRequest dto) throws Exception
	{
		authService.logout(dto.getAccessToken(), token);
		return ResponseEntity.ok()
				.header("Set-Cookie",
						"refreshToken=; httpOnly; secure; path=/; maxAge=0; sameSite=Lax")
				.body(ApiResponse.builder().code(200).message("Logout successful")
						.data(dto.getAccessToken()).build());
	}
}
