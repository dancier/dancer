package net.dancier.service;

import net.dancier.domain.Recommendation;
import net.dancier.domain.User;

import java.util.List;

public interface RecommendationsService {
    /**
     * reads possible precalculated recommendations for the
     * provided user.
     * @param user
     * @return a list of whatever could be recommended to the user
     */
    List<Recommendation> read(User user);
}
