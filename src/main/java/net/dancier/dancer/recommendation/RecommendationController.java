package net.dancier.dancer.recommendation;

import lombok.AllArgsConstructor;
import net.dancier.dancer.core.DancerService;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Recommendable;
import net.dancier.dancer.security.AuthenticatedUser;
import net.dancier.dancer.security.CurrentUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.dancier.dancer.authentication.Constants.ROLE_USER;

@RestController
@RequestMapping(value = "/recommendations", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Secured(ROLE_USER)
    @GetMapping
    public ResponseEntity getTopRecommendations(@CurrentUser AuthenticatedUser authenticatedUser) {
        List<Recommendable> recommendables =
                recommendationService.getRecommendationsForDancerId(authenticatedUser.getDancerIdOrThrow());
      return ResponseEntity.ok(recommendables.stream().map(ModelMapper::recommendableToRecommendationDto));
    };

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handle(Throwable throwable) {
        return ResponseEntity.notFound().build();
    }

}
