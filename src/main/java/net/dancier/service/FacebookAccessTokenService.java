package net.dancier.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.dancier.LoginConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class FacebookAccessTokenService {

    public static final Logger logger = LoggerFactory.getLogger(FacebookAccessTokenService.class);

    public static final String TOKEN_URL = "https://graph.facebook.com/oauth/access_token";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String CLIENT_SECRET_PARAM = "client_secret";
    public static final String GRANT_TYPE_PARAM = "grant_type";
    public static final String GRANT_TYPE_VALUE = "client_credentials";

    private String accessToken = null;

    private String clientId;

    private String clientSecret;

    private Client client;

    public FacebookAccessTokenService(Client client, LoginConfiguration loginConfiguration) {

        this.clientId = loginConfiguration.facebook.clientId;
        this.clientSecret = loginConfiguration.facebook.clientSecret;
        this.client = client;

    }

    public String getAccessToken() {
        if (accessToken!=null) {
            logger.debug("Getting Token from Cache");
            return accessToken;
        } else {
            logger.debug("Getting new Token.");
            accessToken = acquireAccessToken();
            return accessToken;
        }
    }

    private String acquireAccessToken() {
        logger.debug("Getting app access token.");
        WebTarget webTarget = client.target(TOKEN_URL);
        webTarget = webTarget.queryParam(CLIENT_ID_PARAM, clientId);
        webTarget = webTarget.queryParam(CLIENT_SECRET_PARAM, clientSecret);
        webTarget = webTarget.queryParam(GRANT_TYPE_PARAM, GRANT_TYPE_VALUE);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        logger.debug("Target: " + webTarget);

        AccessToken accessToken = invocationBuilder.get(AccessToken.class);
        logger.debug("Got this app access token: " + accessToken);
        return accessToken.getAccessToken();
    }

    @Data
    public static class AccessToken {
        @JsonProperty("access_token")
        private String accessToken;
    }
}
