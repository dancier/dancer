package net.dancier.service;

import net.dancier.domain.Recommendation;
import net.dancier.domain.User;
import net.dancier.domain.dance.Dancer;

import java.util.Arrays;
import java.util.List;

public class MockedRecommendationsService implements RecommendationsService {

    @Override
    public List<Recommendation> get(User user) {
        List<Recommendation> recommendations = Arrays.asList(
            Recommendation.of(Dancer.
                    builder().
                    user(new User()).
                    aboutHim("Hallo").
                    userName("Halbekanne").
                    build())
        );
        return recommendations;
    }

}
