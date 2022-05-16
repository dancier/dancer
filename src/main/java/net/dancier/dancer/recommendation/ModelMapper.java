package net.dancier.dancer.recommendation;

import net.dancier.dancer.core.model.Dance;
import net.dancier.dancer.core.model.Recommendable;
import net.dancier.dancer.school.School;
import net.dancier.dancer.school.SchoolDto;

import java.util.stream.Collectors;

public class ModelMapper {

    public static RecommendationDto recommendableToRecommendationDto(Recommendable recommendable) {
        return new RecommendationDto(recommendable);
    }

    public static SchoolDto schoolToSchoolDto(School school) {
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setName(school.getName());
        schoolDto.setUrl(school.getUrl());
        schoolDto.setSupportedDances(school.getSupportedDances().stream().map(Dance::getName).collect(Collectors.toSet()));
        schoolDto.setCountry(school.getCountry());
        schoolDto.setCity(school.getCity());
        schoolDto.setZipCode(school.getZipCode());
        schoolDto.setProfileImageHash(school.getProfileImageHash());
        return schoolDto;
    }
}
