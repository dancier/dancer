package net.dancier.dancer.recommendation;

import lombok.AllArgsConstructor;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Recommendable;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@AllArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    private final DancerService dancerService;

    @GetMapping
    public ResponseEntity getTopRecommendations(@CurrentUser AuthenticatedUser authenticatedUser) {
        Dancer dancer = dancerService.loadByUserId(authenticatedUser.getId());
        List<Recommendable> recommendables = recommendationService.getRecommendationsForDancerId(dancer.getId());
      return ResponseEntity.ok(recommendables.stream().map(ModelMapper::recommendableToRecommendationDto));
    };

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handle(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }

}