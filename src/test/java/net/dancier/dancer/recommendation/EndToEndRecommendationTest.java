package net.dancier.dancer.recommendation;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.core.DancerRepository;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EndToEndRecommendationTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    DancerRepository dancerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    User user = null;
    @BeforeEach
    public void init() {
        user = userRepository.findByEmail("user@dancier.net").get();
        Dancer dancer = new Dancer();
        dancer.setUserId(user.getId());
        dancer.setDancerName("dancero");
        dancer.setCity("Dortmund");
        dancerRepository.save(dancer);
    }

    @Test
    public void getRecommendation() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/recommendations").cookie(getUserCookie(user.getId())));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
    }

    private Cookie getUserCookie(UUID userId) {
        return new Cookie("jwt-token", jwtTokenProvider.generateJwtToken(userId.toString()));
    }

}
