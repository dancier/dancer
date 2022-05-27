package net.dancier.dancer.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaClient {

    private static final String baseAddress = "https://recaptchaenterprise.googleapis.com/";

    @Value("app.captcha.siteKey")
    private String siteKey;

    @Value("app.captche.apiKey")
    private String apiKey;

    public ResponseEntity validate(String token) {
        CaptchaRequest captchaRequest = new CaptchaRequest();
        CaptchaRequest.Event event = new CaptchaRequest.Event();
        event.token = token;
        event.siteKey = siteKey;
        captchaRequest.event = event;
        ResponseEntity responseEntity = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<CaptchaRequest> httpEntity = new HttpEntity<>(captchaRequest, headers);
            responseEntity = restTemplate
                    .postForEntity(baseAddress + "/v1/projects/clear-safeguard-330719/assessments",
                            httpEntity,
                            Assessment.class);
        } catch (Exception e) {
            System.out.println(e);
        }
        return responseEntity;
    }

    public class Assessment {
        String name;
        Event event;
        TokenProperties tokenProperties;
        class Event {
            String token;
            String siteKey;
            String userAgent;
            String userIpAddress;
            String expectedAction;
            String hashedAccountId;
        }
        class TokenProperties {
            Boolean valid;
            String invalidReason;
            String hostname;
            String action;
            String createTime;
        }
    }

}
