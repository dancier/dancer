package net.dancier.dancer.recommendation;

import net.dancier.dancer.recommendation.dto.Mapper;
import net.dancier.dancer.recommendation.dto.RecommendationDto;
import net.dancier.dancer.recommendation.model.BaseRecommendation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RecommendationServiceClient {

    @Value("${app.recommendation.host}")
    private String host;

    private WebClient webClient;

    static final String BASE_URI = "/recommendations/";

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(host)
                .build();
    }

    // https://www.baeldung.com/spring-webclient-json-list
    public List<BaseRecommendation> getRecommendations(UUID dancerId) {
        return Arrays.stream(webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path(BASE_URI)
                                .path(dancerId.toString())
                            .build()
        )
                .retrieve()
                .bodyToMono(RecommendationDto[].class)
                .block())
                .map(Mapper::recommendationDto2BaseRecommendation)
                .collect(Collectors.toList());
    }

}
