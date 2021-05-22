package net.dancier.service;

import net.dancier.domain.Recommendation;
import net.dancier.domain.User;

import java.util.List;

public interface RecommendationsService {

    List<Recommendation> get(User user);
}
