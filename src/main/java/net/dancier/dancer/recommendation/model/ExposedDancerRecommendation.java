package net.dancier.dancer.recommendation.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ExposedDancerRecommendation {
    UUID id;
    String name;
    String imageHash;
    String about;
    Integer age;
    List<String> dances;

}
