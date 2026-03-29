package vn.nlu.huypham.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JWTConfig {

    final AppConfig appConfig;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(appConfig.getJwt().getKey());
    }

    @Bean
    public JWTVerifier jwtVerifier() {
        return JWT.require(algorithm()).withIssuer("estudy").build();
    }
}
