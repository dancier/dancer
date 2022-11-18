package net.dancier.dancer.recommendation.model;

import lombok.Data;
import net.dancier.dancer.core.model.Dancer;

@Data
public class RecommendationWrapper {
    Dancer dancer;
    Integer score;
}
