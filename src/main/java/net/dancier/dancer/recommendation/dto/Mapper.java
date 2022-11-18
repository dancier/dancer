package net.dancier.dancer.recommendation.dto;

import net.dancier.dancer.recommendation.model.BaseRecommendation;

public class Mapper {

    public static BaseRecommendation recommendationDto2BaseRecommendation(RecommendationDto recommendationDto) {
        BaseRecommendation baseRecommendation = new BaseRecommendation();
        baseRecommendation.setType(BaseRecommendation.Type.valueOf(recommendationDto.getType().name()));
        baseRecommendation.setDancerVersion(recommendationDto.getDancerVersion());
        baseRecommendation.setTargetId(recommendationDto.getTargetId());
        baseRecommendation.setTargetVersion(recommendationDto.getTargetVersion());
        baseRecommendation.setScore(recommendationDto.getScore());
        return baseRecommendation;
    }
}
