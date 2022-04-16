package net.dancier.dancer.recommendation;

import net.dancier.dancer.core.model.Recommendable;

public class ModelMapper {

    public static RecommendationDto recommendableToRecommendationDto(Recommendable recommendable) {
        return new RecommendationDto(recommendable);
    }

}
