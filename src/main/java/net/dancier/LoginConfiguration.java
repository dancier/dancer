package net.dancier;

public class LoginConfiguration {

    public Boolean devEnv;

    public Facebook facebook = new Facebook();

    public static class Facebook {
        public String clientId;
        public String callbackUri;
        public String clientSecret;
    }
}
