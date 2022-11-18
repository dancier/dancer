package net.dancier.dancer.recommendation;

import lombok.Data;

import java.util.UUID;

@Data
public class RecommendationDto {
    public enum Type {
        DANCER,
        SCHOOL,
        EVENT,
        LINK
    }

    private Type type;

    private Integer dancerVersion;

    private UUID targetId;

    private Integer targetVersion;

    private Integer score;

}
