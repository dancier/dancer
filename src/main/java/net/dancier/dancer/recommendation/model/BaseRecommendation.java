package net.dancier.dancer.recommendation.model;

import lombok.Data;

import java.util.UUID;

@Data
public class BaseRecommendation {

    public enum Type {
        DANCER
    }

    private Type type;

    private Integer dancerVersion;

    private UUID targetId;

    private Integer targetVersion;

    private Integer score;

}
