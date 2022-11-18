package net.dancier.dancer.recommendation;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.core.DancerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EndToEndRecommendationTest extends AbstractPostgreSQLEnabledTest {

    @MockBean
    RecommendationServiceClient recommendationServiceClient;

    UUID dancerIdWithRecommendations = null;

    @Autowired
    DancerRepository dancerRepository;
    @BeforeEach
    void init() {
        this.dancerIdWithRecommendations =
                dancerRepository
                        .findByUserId(UUID.fromString("55bbf334-6649-11ed-8f65-5b299f0e161f"))
                        .get()
                        .getId();
        when(recommendationServiceClient
                .getRecommendations(dancerIdWithRecommendations))
                .thenReturn(List.of());
    }
    @Test
    @WithUserDetails("user-with-a-profile@dancier.net")
    public void getRecommendation() throws Exception {
        ResultActions resultActions =
                mockMvc.perform(get("/recommendations"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
    }

}
