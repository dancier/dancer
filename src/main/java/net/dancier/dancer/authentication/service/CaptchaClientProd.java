package net.dancier.dancer.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Profile({"prod", "staging"})
@Component
public class CaptchaClientProd implements CaptchaClient {

    private static final Logger log = LoggerFactory.getLogger(CaptchaClientProd.class);
    private static final String baseAddress = "https://recaptchaenterprise.googleapis.com/";

    @Value("${app.captcha.siteKey}")
    private String siteKey;

    @Value("${app.mail.user}")
    private String mailuser;

    @Value("${app.captcha.apiKey}")
    private String apiKey;

    @Value("${app.captcha.magicToken}")
    private String magicToken;

    @Override
    public ResponseEntity<Assessment> validate(String token) {
        log.info("Validating captcha token: " + token);
        log.info("Magic Token: " + magicToken);
        if (magicToken.equals(token)) {
            return ResponseEntity.ok(createValidAssement());
        }
        CaptchaRequest captchaRequest = new CaptchaRequest();
        CaptchaRequest.Event event = new CaptchaRequest.Event();
        event.token = token;
        event.siteKey = siteKey;
        captchaRequest.event = event;
        ResponseEntity responseEntity = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CaptchaRequest> httpEntity = new HttpEntity<>(captchaRequest, requestHeaders);

            String uri = UriComponentsBuilder
                    .fromHttpUrl(baseAddress)
                    .path("/v1/projects/clear-safeguard-330719/assessments")
                    .queryParam("key", apiKey)
                    .encode()
                    .toUriString();

            responseEntity = restTemplate
                    .postForEntity(uri,
                            httpEntity,
                            Assessment.class);
        } catch (Exception e) {
            System.out.println(e);
        }
        return responseEntity;
    }

    private Assessment createValidAssement() {
        CaptchaClientProd.Assessment assessment = new CaptchaClientProd.Assessment();
        CaptchaClientProd.Assessment.TokenProperties tokenProperties = new CaptchaClientProd.Assessment.TokenProperties();
        tokenProperties.valid = true;
        assessment.tokenProperties = tokenProperties;
        return assessment;
    }

    public static class Assessment {
        public String name;
        public Event event;
        public TokenProperties tokenProperties;
        static class Event {
            public Event() {}
            public String token;
            public String siteKey;
            public String userAgent;
            public String userIpAddress;
            public String expectedAction;
            public String hashedAccountId;
        }
        static class TokenProperties {
            public Boolean valid;
            public String invalidReason;
            public String hostname;
            public String action;
            public String createTime;
        }
    }

}
