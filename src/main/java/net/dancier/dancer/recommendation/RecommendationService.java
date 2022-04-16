package net.dancier.dancer.recommendation;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Recommendable;
import net.dancier.dancer.location.DistanceService;
import net.dancier.dancer.location.ZipCode;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final DancerService dancerService;

    private final DistanceService distanceService;

    public List<Recommendable> getRecommendationsForDancerId(UUID dancerId) {
        List<Recommendable> recommendables = new ArrayList<>();
        recommendables.addAll(dancerService.getAllDancer());
        return recommendables;
    }

    public void computeAll() {

    }

    public List<Dancer> allDancer() {
        return List.of();
    }

    public List<Dancer> allPossibleCandidaties(Dancer dancer) {
        return List.of();
    }

    public RecommendationScore computeScore(Dancer dancerA, Dancer dancerB) {
        RecommendationScore recommendationScore = new RecommendationScore();
        recommendationScore.setDistanceScore(1);
        recommendationScore.setGenderScore(1);
        recommendationScore.setComputedAt(Instant.now());
        return recommendationScore;
    }



}
