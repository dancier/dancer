package net.dancier.dancer.eventlog.token;


import com.fasterxml.jackson.databind.JsonNode;
import io.minio.credentials.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class JwtProvider {
    public static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.s3.oidc.tokenUri}")
    private String tokenUri;

    @Value("${app.s3.oidc.clientId}")
    private String clientId;

    @Value("${app.s3.oidc.clientSecret}")
    private String clientSecret;

    public Jwt getJwt() {
        WebClient webClient = WebClient.create();
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("client_id", clientId);
        multiValueMap.add("client_secret", clientSecret);
        multiValueMap.add("grant_type", "client_credentials");

        WebClient.ResponseSpec responseSpec =  webClient
                .post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(multiValueMap))
                .retrieve();
        JsonNode result =  responseSpec.bodyToMono(JsonNode.class).block();
        log.info(result.toString());
        String jwtToken = result.get("access_token").asText();
        Integer expiresIn = result.get("expires_in").asInt();
        return new Jwt(jwtToken, expiresIn);
    }

}
