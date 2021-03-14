package net.dancier.resources.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.dancier.LoginConfiguration;
import lombok.Data;
import lombok.SneakyThrows;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;

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


    public final static String REQUESTED_SCOPES = "email,public_profile";
    public final static String FACEBOOK_BASE = "https://www.facebook.com/v9.0/dialog/oauth?";


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

    public LoginResource(Client client, LoginConfiguration loginConfiguration) {
        this.client = client;
        this.loginConfiguration = loginConfiguration;
    }

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
        StringBuilder sb = new StringBuilder();
        if (getParam(request.getParameterMap(), OIDC_PARAM_CODE).isPresent()) {
            String accessToken = exchangeToken(request.getParameterMap().get(OIDC_PARAM_CODE)[0]);
            String userId = getUserId(accessToken);
            FacebookProfile facebookProfile = getFacebookProfile(userId, accessToken);
            System.out.println("Got Profile");
            DefaultJwtCookiePrincipal cookiePrincipal = new DefaultJwtCookiePrincipal(facebookProfile.getName());
            cookiePrincipal.addInContext(requestContext);
            return Response.seeOther(UriBuilder.fromPath("https://dancier.net").build()).build();
        }
        return null;
    }
    private String exchangeToken(String code) {
        System.out.println("Exchange Token:" + code);
        WebTarget webTarget = client.target(OIDC_TOKEN_ENDPOINT);
        webTarget = webTarget.queryParam(OIDC_CLIENT_ID, loginConfiguration.facebook.clientId);
        webTarget = webTarget.queryParam(OIDC_REDIRECT_URI, loginConfiguration.facebook.callbackUri);
        System.out.println("use cs: " + loginConfiguration.facebook.clientSecret);
        webTarget = webTarget.queryParam(OIDC_CLIENT_SECRET, loginConfiguration.facebook.clientSecret);
        webTarget = webTarget.queryParam(OIDC_PARAM_CODE, code);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        FacebookAccessToken response = invocationBuilder.get(FacebookAccessToken.class);
        System.out.print("access tokken: " + response.accessToken);
        return response.getAccessToken();
    }

    private String getUserId(String accessToken) {
        WebTarget webTarget = client.target(OIDC_VERIFY_ENDPOINT);
        webTarget = webTarget.queryParam(OIDC_INPUT_TOKEN, accessToken);
        webTarget = webTarget.queryParam(OIDC_ACCESS_TOKEN, accessToken);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        FacebookVerify response = invocationBuilder.get(FacebookVerify.class);
        System.out.println("validated !!!!!!!!!!!!!: " + response.getData().getType());
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
