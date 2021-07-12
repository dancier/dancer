package net.dancier.service;

import net.dancier.domain.Recommendation;
import net.dancier.domain.User;
import net.dancier.domain.dance.Dancer;
import net.dancier.domain.dance.School;

import java.util.Arrays;
import java.util.List;

/**
 * Just returns some static recommendations, without using db
 * connections and no calculations at all.
 */
public class MockedRecommendationsService implements RecommendationsService {

    @Override
    public List<Recommendation> read(User user) {
        List<Recommendation> recommendations = Arrays.asList(
            Recommendation.of(Dancer.
                    builder().
                    user(new User()).
                    aboutHim("Hallo").
                    userName("Halbekanne").
                    build()),
                Recommendation.of(School.
                        builder().
                        name("Pietra").
                        build())
        );
        return recommendations;
    }

}
