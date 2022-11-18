package net.dancier.dancer.recommendation;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.model.Recommendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private static Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final RecommendationServiceClient recommendationServiceClient;

    // https://www.baeldung.com/java-collectors-tomap
    public List<Recommendable> getRecommendationsForDancerId(UUID dancerId) {
        List<RecommendationDto> recommendationDtos = recommendationServiceClient.getRecommendations(dancerId);
        log.info("Got : " + recommendationDtos);
        Map<UUID, Integer> dancerId2Version = recommendationDtos
                .stream()
                .filter(p -> RecommendationDto.Type.DANCER.equals(p.getType()))
                .collect(Collectors.toMap(RecommendationDto::getTargetId, RecommendationDto::getTargetVersion));
        // Test what happens when we have
        log.info("With: " + dancerId2Version);
        List<Recommendable> recommendables = new ArrayList<>();
        return recommendables;
    }

}
