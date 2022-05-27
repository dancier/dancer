package net.dancier.dancer.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaService {
    private final CaptchaClient captchaClient;
    public void verifyToken(String token) {
        ResponseEntity<CaptchaClient.Assessment> responseEntity = captchaClient.validate(token);
        System.out.print(responseEntity);
    }
}
