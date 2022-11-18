package net.dancier.dancer.recommendation.model;

import lombok.Data;

@Data
public class RecommendationWrapper {
    Dancer dancer;
    Integer score;
}
