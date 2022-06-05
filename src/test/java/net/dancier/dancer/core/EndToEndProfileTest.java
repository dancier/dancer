package net.dancier.dancer.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dancier.dancer.AbstractPostgreSQLEnabledTest;
import net.dancier.dancer.core.dto.DanceProfileDto;
import net.dancier.dancer.core.dto.ProfileDto;
import net.dancier.dancer.core.model.*;
import net.dancier.dancer.location.ZipCode;
import net.dancier.dancer.location.ZipCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EndToEndProfileTest extends AbstractPostgreSQLEnabledTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ZipCodeRepository zipCodeRepository;

    @BeforeEach
    void init() {
        ZipCode zipCode = new ZipCode();
        zipCode.setCity("Dortmund");
        zipCode.setZipCode("44339");
        zipCode.setCountry("GER");
        zipCode.setLatitude(1d);
        zipCode.setLongitude(2d);
        zipCodeRepository.save(zipCode);
    }

    @Test
    @WithUserDetails("user@dancier.net")
    void fromVirginProfileToPopulatedProfile() throws Exception {
        ResultActions initialGetOfProfile = mockMvc.perform(get("/profile"));
        initialGetOfProfile.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.gender").isEmpty())
                .andExpect(jsonPath("$.size").isEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty());

        ProfileDto profileDto = objectMapper.readValue(
                initialGetOfProfile
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ProfileDto.class);

        DanceProfileDto danceProfileDto = new DanceProfileDto();
        danceProfileDto.setDance("Tango");
        danceProfileDto.setLevel(Level.BASIC);
        danceProfileDto.setLeading(Leading.FOLLOW);


        profileDto.setGender(Gender.DIVERS);
        profileDto.setBirthDate(new Date());
        profileDto.setAbleTo(Set.of(danceProfileDto));
        profileDto.setWantsTo(Set.of(danceProfileDto));
        profileDto.setZipCode("44339");
        profileDto.setCountry("GER");

        ResultActions changeDaProfile = mockMvc
                .perform(put("/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(profileDto))
                );

        changeDaProfile.andExpect(status().isOk());

        ResultActions getTheProfileAfterChangedProperties = mockMvc.perform(get("/profile"));
        getTheProfileAfterChangedProperties
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.gender").isNotEmpty())
                .andExpect(jsonPath("$.birthDate").isNotEmpty())
                .andExpect(jsonPath("$.wantsTo").isNotEmpty())
                .andExpect(jsonPath("$.ableTo").isNotEmpty())
                .andExpect(jsonPath("$.city").value("Dortmund"))
                .andExpect(jsonPath("$.country").value("GER"));

    }

}
