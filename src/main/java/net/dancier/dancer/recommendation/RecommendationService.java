package net.dancier.dancer.recommendation;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Recommendable;
import net.dancier.dancer.school.SchoolService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    public List<Recommendable> getRecommendationsForDancerId(UUID dancerId) {
        List<Recommendable> recommendables = new ArrayList<>();
        return recommendables;
    }

}
