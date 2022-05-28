package net.dancier.dancer.authentication.service;

import lombok.Data;

public class CaptchaRequest {
    public Event event;
    static class Event {
        public String token;
        public String siteKey;
    }
}

