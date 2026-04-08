package vn.nlu.huypham.app.service.imp;

import java.util.Set;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.constant.Roles;
import vn.nlu.huypham.app.dto.request.OTPMailContent;
import vn.nlu.huypham.app.dto.request.RegisterBasic;
import vn.nlu.huypham.app.dto.request.RegisterOTPBasic;
import vn.nlu.huypham.app.dto.response.ATAndRT;
import vn.nlu.huypham.app.entity.MailOTP;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.RoleRepo;
import vn.nlu.huypham.app.repository.UserRepo;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.AuthService;
import vn.nlu.huypham.app.service.JWTService;
import vn.nlu.huypham.app.service.MailOTPService;
import vn.nlu.huypham.app.service.ReCaptchaService;
import vn.nlu.huypham.app.service.RedisService;
import vn.nlu.huypham.app.service.JWTService.JWTInfo;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class AuthServiceImp implements AuthService {
    private final AppConfig appConfig;

    final AuthenticationManager authManager;

    final GoogleIdTokenVerifier ggVerifier;

    final JWTService jwtService;
    final MailOTPService mailService;
    final ReCaptchaService reCaptchaService;

    final UserRepo userRepo;
    final RoleRepo roleRepo;
    final RedisService redisService;

    @Override
    public ATAndRT auth(String username, String password) throws AppException {
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            return ATAndRT.builder()
                    .accessToken(jwtService.generateAT(userPrincipal.getUser()))
                    .refreshToken(jwtService.generateRT(userPrincipal.getUser()))
                    .build();
        } catch (BadCredentialsException e) {
            throw Errors.USERNAME_OR_PASSWORD_INVALID;
        } catch (DisabledException | LockedException e) {
            throw Errors.ACCOUNT_NOT_ACTIVATED;
        } catch (Exception e) {
            log.error("Error during authentication: {}", e);
            throw Errors.AUTH_FAILED;
        }
    }

    @Override
    @Transactional
    public ATAndRT auth(String googleIDToken) throws AppException {
        try {
            GoogleIdToken idToken = ggVerifier.verify(googleIDToken);

            if (idToken == null) {
                log.error("Invalid ID token.");
                throw Errors.AUTH_FAILED;
            }

            Payload payload = idToken.getPayload();
            String email = (String) payload.get("email");
            String picture = (String) payload.get("picture");
            String name = (String) payload.get("name");

            User user = userRepo.findByEmail(email).orElseGet(() -> {
                User newUser = User.builder()
                        .email(email)
                        .username(email)
                        .displayName(name)
                        .avatar(picture)
                        .isEnabled(true)
                        .roles(Set.of(roleRepo.findByName(Roles.STUDENT).orElseThrow(() -> Errors.AUTH_FAILED)))
                        .isAccountNonLocked(true)
                        .build();
                return userRepo.save(newUser);
            });

            return ATAndRT.builder()
                    .accessToken(jwtService.generateAT(user))
                    .refreshToken(jwtService.generateRT(user))
                    .build();
        } catch (Exception e) {
            log.error("Error during authentication: {}", e);
            throw Errors.AUTH_FAILED;
        }
    }

    @Override
    public UUID preRegister(RegisterOTPBasic dto) throws AppException {

        if (!reCaptchaService.verify(dto.getReCaptchaToken()))
            throw Errors.RECAPTCHA_INVALID;

        if (userRepo.existsByUsernameOrEmail(dto.getUsername(), dto.getEmail()))
            throw Errors.PRE_REGISTER_FAILED;

        MailOTP mailOTP = mailService.createRegisterOTP(dto);

        mailService.sendOTPMail(dto.getEmail(), "Account Registration OTP",
                OTPMailContent.builder()
                        .content("This code will expire after %d minutes"
                                .formatted(appConfig.getMail().getOtpExp() / 60))
                        .name(dto.getUsername())
                        .otp(mailOTP.getOtp())
                        .build());

        return mailOTP.getId();
    }

    @Override
    @Transactional
    public ATAndRT register(RegisterBasic dto, String otp) throws AppException {
        User user = mailService.validateOTP(dto.getMailOTPId(), dto.getEmail(), otp);
        user.setRoles(Set.of(roleRepo.findByName(Roles.STUDENT).orElseThrow(() -> Errors.REGISTER_FAILED)));
        userRepo.save(user);

        return ATAndRT.builder()
                .accessToken(jwtService.generateAT(user))
                .refreshToken(jwtService.generateRT(user))
                .build();
    }

    @Override
    public void logout(String accessToken, String refreshToken) throws AppException {
        try {
            JWTInfo info = jwtService.extractFrom(accessToken);
            redisService.addATBlacklist(info.getId(), info.getExpiredAt());
        } catch (Exception e) {
            log.warn("Invalid access token during logout: {}", e.getMessage());
        }
        jwtService.invokeRT(refreshToken);

    }
}
