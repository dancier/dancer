package net.dancier.dancer.authentication.service;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.CaptchaException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaService {
    private final CaptchaClient captchaClient;
    public void verifyToken(String token) {
        if (token==null || token.isBlank()) {
            throw new CaptchaException("No Captcha Token provided.");
        }
        ResponseEntity<CaptchaClientProd.Assessment> responseEntity = captchaClient.validate(token);
        if (responseEntity.getStatusCode().is4xxClientError()) {
            throw new CaptchaException("Technical Problem with Captcha processing.");
        }
        if (!responseEntity.getBody().tokenProperties.valid) {
            throw new CaptchaException("Token ist not valid. Maybe expired?");
        }
    }
}
