package net.dancier.dancer.recommendation.dto;

import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.recommendation.model.BaseRecommendation;
import net.dancier.dancer.recommendation.model.RecommendationWrapper;

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

    public static ExposedRecommendationDto recommendationWrapper2ExposedRecommendationDto(RecommendationWrapper recommendationWrapper) {
        ExposedRecommendationDto exposedRecommendationDto = new ExposedRecommendationDto();
        switch (recommendationWrapper.getPayload()) {
            case Dancer dancer -> {
                exposedRecommendationDto.setType("DANCER");
                ExposedRecommendationDto.DancerPayload dancerPayload = new ExposedRecommendationDto.DancerPayload();
                dancerPayload.setId(dancer.getId().toString());
                dancerPayload.setName(dancer.getDancerName());
                dancerPayload.setImageHash(dancer.getProfileImageHash());
                dancerPayload.setAbout(dancer.getAboutMe());
                dancerPayload.setAge(null);
                dancerPayload.setZip(dancer.getZipCode());
                dancerPayload.setCity(dancer.getCity());
                dancerPayload.setDances(null);
                dancerPayload.setScore(recommendationWrapper.getScore());
                exposedRecommendationDto.setPayload(dancerPayload);
            }
            case default -> {
                throw  new IllegalStateException();
            }
        }
        return exposedRecommendationDto;
    }
}
