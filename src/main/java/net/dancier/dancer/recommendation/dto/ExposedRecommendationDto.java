package net.dancier.dancer.recommendation.dto;

import lombok.Data;

@Data
public class ExposedRecommendationDto {
    String type;
    Object payload;
    @Data
    public static class DancerPayload {
        String id;
        String name;
    }

}

