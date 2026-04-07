package vn.nlu.huypham.app.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.service.ReCaptchaService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ReCaptchaServiceImp implements ReCaptchaService {

    final AppConfig appConfig;
    final WebClient webClient = WebClient.create();

    @Override
    public boolean verify(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        try {
            CloudflareResponse response = webClient.post()
                    .uri(appConfig.getCloudflare().getVerifyUrl())
                    .bodyValue(new CloudflareRequest(appConfig.getCloudflare().getSecretKey(), token))
                    .retrieve()
                    .bodyToMono(CloudflareResponse.class)
                    .block();

            if (response == null) {
                log.warn("Null response from Cloudflare");
                return false;
            }

            if (!response.isSuccess()) {
                log.warn("Cloudflare verification failed: {}", response.getErrorCodes());
                return false;
            }

            return true;
        } catch (WebClientResponseException e) {
            log.error("Error during Cloudflare verification: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during Cloudflare verification", e);
            return false;
        }

    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor
    private static class CloudflareRequest {
        String secret;
        String response;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor
    private static class CloudflareResponse {
        List<String> errorCodes;
        boolean success;
    }

}