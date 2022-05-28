package net.dancier.dancer.authentication.service;

import org.springframework.http.ResponseEntity;

public interface CaptchaClient {

    ResponseEntity validate(String token);
}
