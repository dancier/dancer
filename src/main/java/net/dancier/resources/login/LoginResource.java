package net.dancier.resources.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.SneakyThrows;
import net.dancier.LoginConfiguration;
import net.dancier.domain.User;
import net.dancier.service.FacebookAccessTokenService;
import net.dancier.service.UserService;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.stream.Collectors.joining;


@Path("/login")
public class LoginResource {

    private final Logger logger = LoggerFactory.getLogger(LoginResource.class);

    private UserService userService;
    private FacebookAccessTokenService facebookAccessTokenService;

    public final static String REQUESTED_SCOPES = "email,public_profile";
    public final static String FACEBOOK_BASE = "https://www.facebook.com/v9.0/dialog/oauth?";

    public final static String MOCKED_FB_USER = "mocked_fb_user";

    public final static String OIDC_PARAM_CODE = "code";
    public final static String OIDC_PARAM_ERROR_REASON = "error_reason";
    public final static String OIDC_CLIENT_ID = "client_id";
    public final static String OIDC_REDIRECT_URI = "redirect_uri";
    public final static String OIDC_CLIENT_SECRET = "client_secret";
    public final static String OIDC_INPUT_TOKEN = "input_token";
    public final static String OIDC_ACCESS_TOKEN = "access_token";

    public final static String OIDC_TOKEN_ENDPOINT = "https://graph.facebook.com/v9.0/oauth/access_token";
    public final static String OIDC_VERIFY_ENDPOINT = "https://graph.facebook.com/debug_token";
    public final static String GRAPH_ENDPOINT = "https://graph.facebook.com/";
    public final static String GRAPH_FIELDS = "fields";

    public LoginResource(Client client, LoginConfiguration loginConfiguration, UserService userService, FacebookAccessTokenService facebookAccessTokenService) {
        this.client = client;
        this.loginConfiguration = loginConfiguration;
        this.userService = userService;
        this.facebookAccessTokenService = facebookAccessTokenService;
    }

    @Data
    public static class FacebookAccessToken {
        public FacebookAccessToken() {}
        @JsonProperty("access_token")
        public String accessToken;

    }

    @Data
    public static class FacebookVerify {

        public FacebookVerify() {}

        @lombok.Data
        public static class Data {
            public Data() {}

            @JsonProperty("app_id")
            String appId;

            @JsonProperty("type")
            String type;

            @JsonProperty("is_valid")
            Boolean isValid;

            @JsonProperty("scopes")
            List<String> scopes;

            @JsonProperty("user_id")
            String userId;
        }
        @JsonProperty("data")
        Data data;
    }

    @Data
    public static class FacebookProfile {

        public FacebookProfile() {}

        @JsonProperty("email")
        public String email;

        @JsonProperty("id")
        public String id;

        @JsonProperty("name")
        public String name;
    }

    private Client client;
    private LoginConfiguration loginConfiguration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OidcProvider> base() throws UnsupportedEncodingException {
        OidcProvider oidcProvider = new OidcProvider(
                1,
                "Facebook",
                "login mit Deinem Facebook Account",
                "",
                constructFacebookLink(
                        loginConfiguration.facebook.clientId,
                        loginConfiguration.facebook.callbackUri,
                        "",
                        UUID.randomUUID().toString()));
        return Arrays.asList(oidcProvider);
    }

    @SneakyThrows
    @GET
    @Path("callback")
    public Response callback(@Context HttpServletRequest request, @Context ContainerRequestContext requestContext) {
        logger.debug("Received callback");
        if (loginConfiguration.devEnv && !getParam(request.getParameterMap(), OIDC_PARAM_CODE).isPresent()) {
            logger.debug("Dev mode");
            String foreignUserId = getParam(request.getParameterMap(),MOCKED_FB_USER).get();
            User user = userService.assignUser(User.IdProvider.FACEBOOK, foreignUserId, "bar");
            DefaultJwtCookiePrincipal cookiePrincipal = new DefaultJwtCookiePrincipal(user.getId().toString());
            cookiePrincipal.addInContext(requestContext);
            Response.ok();
        }
        if (getParam(request.getParameterMap(), OIDC_PARAM_CODE).isPresent()) {
            logger.debug("Code is present. Exchanging Token.");
            String accessToken = exchangeToken(request.getParameterMap().get(OIDC_PARAM_CODE)[0]);
            logger.debug("Got the token.");
            String appAccessToken = facebookAccessTokenService.getAccessToken();
            logger.debug("Got this app access token: " + appAccessToken) ;
            String userId = getUserId(accessToken, appAccessToken);
            logger.debug("Got Userid.");
            FacebookProfile facebookProfile = getFacebookProfile(userId, accessToken);
            logger.debug("Got Profile");
            User user = userService.assignUser(User.IdProvider.FACEBOOK, facebookProfile.id, facebookProfile.email);
            logger.debug("Using user: " + user.getId());
            DefaultJwtCookiePrincipal cookiePrincipal = new DefaultJwtCookiePrincipal(user.getId().toString());
            cookiePrincipal.addInContext(requestContext);
            return Response.seeOther(UriBuilder.fromPath("https://dancier.net").build()).build();
        }
        return null;
    }

    private String exchangeToken(String code) {
        logger.debug("Exchange Token:" + code);
        WebTarget webTarget = client.target(OIDC_TOKEN_ENDPOINT);
        webTarget = webTarget.queryParam(OIDC_CLIENT_ID, loginConfiguration.facebook.clientId);
        webTarget = webTarget.queryParam(OIDC_REDIRECT_URI, loginConfiguration.facebook.callbackUri);
        logger.debug("use cs: " + loginConfiguration.facebook.clientSecret);
        webTarget = webTarget.queryParam(OIDC_CLIENT_SECRET, loginConfiguration.facebook.clientSecret);
        webTarget = webTarget.queryParam(OIDC_PARAM_CODE, code);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        FacebookAccessToken response = invocationBuilder.get(FacebookAccessToken.class);
        return response.getAccessToken();
    }

    private String getUserId(String accessToken, String appAccessToken) {
        logger.debug("Getting Userid from accesstoken.");
        WebTarget webTarget = client.target(OIDC_VERIFY_ENDPOINT);
        webTarget = webTarget.queryParam(OIDC_INPUT_TOKEN, accessToken);
        webTarget = webTarget.queryParam(OIDC_ACCESS_TOKEN, appAccessToken);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        FacebookVerify response = invocationBuilder.get(FacebookVerify.class);
        logger.debug("Got this Userid from Accesstoken: "+ response);
        return response.data.userId;
    }

    private String constructFacebookLink(String clientId,
                                         String callbackUrl,
                                         String redirectUrl,
                                         String state) throws UnsupportedEncodingException {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("client_id", encodeValue(clientId));
        requestParams.put("redirect_uri", encodeValue(callbackUrl));
        requestParams.put("state", encodeValue(redirectUrl + "-" + state));
        requestParams.put("scopes", encodeValue(REQUESTED_SCOPES));

        return requestParams.keySet().stream()
                .map(key -> key + "=" + requestParams.get(key))
                .collect(joining("&", FACEBOOK_BASE, ""));
    }

    private String encodeValue(String rawString) throws UnsupportedEncodingException {
        return URLEncoder.encode(rawString, StandardCharsets.UTF_8.toString());
    }

    public FacebookProfile getFacebookProfile(String id, String access_token) {
        WebTarget webTarget = client.target(GRAPH_ENDPOINT +id);
        webTarget = webTarget.queryParam(GRAPH_FIELDS, "email,id,name");
        webTarget = webTarget.queryParam(OIDC_ACCESS_TOKEN, access_token);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        FacebookProfile response = invocationBuilder.get(FacebookProfile.class);
        return response;

    }

    private Optional<String> getParam(Map<String, String[]> parameterMap, String key) {
        if (parameterMap.containsKey(key)) {
            return Optional.of(parameterMap.get(key)[0]);
        } else {
            return Optional.empty();
        }
    }
}
