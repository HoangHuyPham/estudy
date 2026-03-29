package vn.nlu.huypham.app.service.imp;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.dto.request.ClickableMailContent;
import vn.nlu.huypham.app.dto.response.ATAndRT;
import vn.nlu.huypham.app.entity.RefreshToken;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.RefreshTokenRepo;
import vn.nlu.huypham.app.service.JWTService;
import vn.nlu.huypham.app.service.MailOTPService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JWTServiceImp implements JWTService {
    final JWTVerifier jwtVerifier;
    final Algorithm algorithm;
    final AppConfig appConfig;

    final RefreshTokenRepo refreshTokenRepo;

    final MailOTPService mailService;

    @Override
    public JWTInfo extractFrom(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        return JWTInfo.builder()
                .issuer(decodedJWT.getIssuer())
                .id(UUID.fromString(decodedJWT.getId()))
                .avatar(decodedJWT.getClaim("avatar").asString())
                .role(decodedJWT.getClaim("role").asString())
                .username(decodedJWT.getSubject())
                .build();
    }

    @Override
    public String generateAT(User user) {
        Instant now = Instant.now();
        Instant expireAt = now.plus(appConfig.getJwt().getAccessExp(), ChronoUnit.SECONDS);

        Builder tokenBuilder = JWT.create()
                .withIssuer(appConfig.getJwt().getIssuer())
                .withSubject(user.getUsername())
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expireAt));

        if (user.getAvatar() != null) {
            tokenBuilder.withClaim("avatar", user.getAvatar());
        }
        if (user.getRole() != null) {
            tokenBuilder.withClaim("role", user.getRole().getName().name().toUpperCase());
        }

        return tokenBuilder.sign(algorithm);
    }

    @Override
    public UUID generateRT(User user) {
        RefreshToken refreshToken = refreshTokenRepo.save(
                RefreshToken.builder()
                        .user(user)
                        .expiredAt(Instant.now().plusMillis(appConfig.getJwt().getRefreshExp()).toEpochMilli())
                        .build());
        return refreshToken.getId();
    }

    @Override
    @Transactional
    public ATAndRT rotateRT(String refreshToken) throws AppException {
        RefreshToken oldRT = refreshTokenRepo.findById(UUID.fromString(refreshToken))
                .orElseThrow(() -> Errors.REFRESH_TOKEN_INVALID);

        if (oldRT.getExpiredAt() < Instant.now().toEpochMilli())
            throw Errors.REFRESH_TOKEN_INVALID;

        User user = oldRT.getUser();
        if (user == null)
            throw Errors.REFRESH_TOKEN_INVALID;

        if (oldRT.isUsed()) {
            log.warn("Detected refresh token reuse! User: {}, Warning email was sent", user.getUsername());
            mailService.sendClickableMail(user.getEmail(), "Detected unauthorised access",
                    ClickableMailContent.builder()
                            .name(user.getUsername())
                            .content(
                                    "We have detected unauthorised access to your account. If this was not you, please change your password.")
                            .buttonText("Change Password")
                            .buttonUrl("http://localhost:3000/change-password")
                            .build());
            throw Errors.REFRESH_TOKEN_INVALID;
        }

        oldRT.setUsed(true);
        refreshTokenRepo.save(oldRT);

        return ATAndRT.builder()
                .accessToken(generateAT(user))
                .refreshToken(generateRT(user))
                .build();
    }
}