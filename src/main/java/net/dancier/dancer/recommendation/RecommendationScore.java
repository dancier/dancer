package net.dancier.dancer.recommendation;

import lombok.Data;
import net.dancier.dancer.core.model.Dancer;
import org.springframework.data.util.Pair;

import java.time.Instant;

@Data
public class RecommendationScore {

    Pair<Dancer, Dancer> participants;

    private Instant computedAt;

    private Integer distanceScore;
    private Integer genderScore;
    private Integer ableWantScore;

}
