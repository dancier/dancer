package net.dancier.dancer.authentication.service;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Profile({"dev", "test"})
@Component
public class CaptchaClientStub implements CaptchaClient{
    @Override
    public ResponseEntity validate(String token) {
        CaptchaClientProd.Assessment assessment = new CaptchaClientProd.Assessment();
        CaptchaClientProd.Assessment.TokenProperties tokenProperties = new CaptchaClientProd.Assessment.TokenProperties();
        switch (token) {
            case "invalid": tokenProperties.valid = false; break;
            default: tokenProperties.valid = true;
        }
        assessment.tokenProperties = tokenProperties;
        return ResponseEntity.ok(assessment);
    }
}
