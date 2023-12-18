package net.dancier.dancer.recommendation.dto;

import net.dancier.dancer.core.model.Dance;
import net.dancier.dancer.core.model.DanceProfile;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.recommendation.model.BaseRecommendation;
import net.dancier.dancer.recommendation.model.RecommendationWrapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                dancerPayload.setAge(age(dancer.getBirthDate()));
                dancerPayload.setZip(dancer.getZipCode());
                dancerPayload.setCity(dancer.getCity());
                dancerPayload.setDances(getDances(dancer));
                dancerPayload.setScore(recommendationWrapper.getScore());
                exposedRecommendationDto.setPayload(dancerPayload);
            }
            default -> {
                throw  new IllegalStateException();
            }
        }
        return exposedRecommendationDto;
    }

    private static List<String> getDances(Dancer dancer) {
        Set<String> result = new HashSet<>();
        result.addAll(
                dancer.getAbleTo().stream()
                        .map(DanceProfile::getDance)
                        .map(Dance::getName).collect(Collectors.toSet())
        );
        result.addAll(
                dancer.getWantsTo().stream()
                        .map(DanceProfile::getDance)
                        .map(Dance::getName).collect(Collectors.toSet())
        );
        return result.stream().toList();
    }
    private static Integer age(LocalDate birthdate) {
        LocalDate now = LocalDate.now();
        if (birthdate!=null) {
            return Period.between(
                    birthdate , now)
                    .getYears();
        } else {
            return 0;
        }
    }
}
