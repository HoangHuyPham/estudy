package vn.nlu.huypham.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@ConfigurationProperties(prefix = "app")
@Component
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppConfig {
    JWT jwt;
    Google google;
    Mail mail;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class JWT {
        String issuer;
        String key;
        long refreshExp; // seconds
        long accessExp; // seconds
    }
    
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Google {
        OAuth2 oauth2;
        API api;
        Recaptcha recaptcha;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OAuth2 {
        String clientId;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class API {
        String key;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Recaptcha {
        String siteKey;
        String verifyUrl;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Mail {
        long otpExp; // seconds 
    }
}
