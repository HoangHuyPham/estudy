package vn.nlu.huypham.app.service;

import java.time.Instant;
import java.util.UUID;

import com.auth0.jwt.exceptions.JWTVerificationException;

import lombok.Builder;
import lombok.Data;
import vn.nlu.huypham.app.dto.response.ATAndRT;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface JWTService {
    JWTInfo extractFrom(String token) throws JWTVerificationException;

    String generateAT(User user);

    UUID generateRT(User user);

    ATAndRT rotateRT(String refreshToken) throws AppException;

    void invokeRT(String refreshToken) throws AppException;

    @Data
    @Builder
    public static class JWTInfo {
        UUID id;
        String issuer;
        String username;
        String avatar;
        String role;
        Instant expiredAt;
    }
}
