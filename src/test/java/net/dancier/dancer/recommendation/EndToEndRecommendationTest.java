package net.dancier.dancer.recommendation;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.core.DancerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EndToEndRecommendationTest extends AbstractPostgreSQLEnabledTest {

    @Test
    @WithUserDetails("user-with-a-profile@dancier.net")
    public void getRecommendation() throws Exception {
        ResultActions resultActions =
                mockMvc.perform(get("/recommendations"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
    }

}
