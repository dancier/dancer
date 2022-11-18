package net.dancier.dancer.recommendation.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExposedRecommendationDto {
    String type;
    Object payload;
    @Data
    public static class DancerPayload {
        String id;
        String name;
        String imageHash;
        String about;
        Integer age;
        String zip;
        String city;
        List<String> dances;
        Integer score;
    }

}

