package net.dancier.dancer.recommendation.model;

import lombok.Data;

@Data
public class RecommendationWrapper<T> {
    T payload;
    Integer score;
}
