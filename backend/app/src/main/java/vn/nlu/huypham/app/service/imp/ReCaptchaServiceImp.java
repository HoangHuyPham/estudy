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

    private static final String EXPECTED_ACTION = "USER_ACTION";
    private static final double MIN_SCORE = 0.5;

    final AppConfig appConfig;
    final WebClient webClient = WebClient.create();

    @Override
    public boolean verify(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String verifyUri = appConfig.getGoogle().getRecaptcha().getVerifyUrl()
                + appConfig.getGoogle().getApi().getKey();
        VerifyRequest request = new VerifyRequest(
                new VerifyEvent(token, EXPECTED_ACTION, appConfig.getGoogle().getRecaptcha().getSiteKey()));

        try {
            VerifyResponse response = webClient.post()
                    .uri(verifyUri)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(VerifyResponse.class)
                    .block();

            if (response == null || response.tokenProperties == null || response.riskAnalysis == null) {
                return false;
            }

            boolean isValidToken = response.tokenProperties.valid;
            boolean hasAcceptableScore = response.riskAnalysis.score != null
                    && response.riskAnalysis.score >= MIN_SCORE;
            log.info("reCAPTCHA verification result: valid={}, score={}, reasons={}",
                    isValidToken, response.riskAnalysis.score, response.riskAnalysis.reasons);
            return isValidToken && hasAcceptableScore;
        } catch (WebClientResponseException ex) {
            log.warn("reCAPTCHA verification failed with status {}: {}", ex.getStatusCode(),
                    ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            log.error("Unexpected error during reCAPTCHA verification", ex);
            return false;
        }
    }

    @Data
    @AllArgsConstructor
    static class VerifyRequest {
        VerifyEvent event;
    }

    @Data
    @AllArgsConstructor
    static class VerifyEvent {
        String token;
        String expectedAction;
        String siteKey;
    }

    @Data
    @AllArgsConstructor
    static class VerifyResponse {
        String name;
        VerifyEvent event;
        RiskAnalysis riskAnalysis;
        TokenProperties tokenProperties;
    }

    @Data
    @AllArgsConstructor
    static class RiskAnalysis {
        Double score;
        List<String> reasons;
    }

    @Data
    @AllArgsConstructor
    static class TokenProperties {
        Boolean valid;
        String invalidReason;
        String action;
        String createTime;
    }
}