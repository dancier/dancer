package net.dancier.dancer.recommendation;

import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.core.ProfileService;
import net.dancier.dancer.core.dto.ProfileDto;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EndToEndRecomendationTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    ProfileService profileService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void init() {
        User user = userRepository.findByEmail("user@dancier.net").get();
        ProfileDto profileDto = profileService.getProfileByUserId(user.getId());
        profileService.updateProfileForUserId(user.getId(), profileDto);
    }

    @Test
    @WithUserDetails("user@dancier.net")
    public void getRecommendation() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/recommendations"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        String bla = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(bla).isNotNull();
    }
}
