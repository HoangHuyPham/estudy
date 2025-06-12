package vn.edu.hcmuaf.be.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "estudy")
@Getter
@Setter
public class CustomConfig {
    private Jwt jwt;

    @Getter
    @Setter
    public static class Jwt{
        private String key;
    }
}

