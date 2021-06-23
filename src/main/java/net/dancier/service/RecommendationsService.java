package net.dancier.service;

import net.dancier.domain.Recommendation;
import net.dancier.domain.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface RecommendationsService {
    /**
     * returns whatever could be recommended (see {@link net.dancier.domain.Recommendable} and {@link Recommendation})
     * to the user.
     * @param user recommendations are specifiy to a user
     * @return a list of whatever could be recommended to the user
     */
    List<Recommendation> read(@NotNull User user);
}
