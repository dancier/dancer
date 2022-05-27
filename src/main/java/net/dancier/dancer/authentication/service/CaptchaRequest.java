package net.dancier.dancer.authentication.service;

public class CaptchaRequest {

    public CaptchaRequest() {}
    Event event;
    static class Event {
        public Event() {}
        String token;
        String siteKey;
    }
}

