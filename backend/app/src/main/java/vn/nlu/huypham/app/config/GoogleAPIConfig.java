package vn.nlu.huypham.app.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GoogleAPIConfig {
    final AppConfig appConfig;

    @Bean
    public GoogleIdTokenVerifier ggVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                new GsonFactory())
                .setAudience(Collections.singletonList(appConfig.getGoogle().getOauth2().getClientId()))
                .build();
    }
}
