package net.dancier.dancer.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BaseUrlConfiguration {

    @Bean
    @Profile("dev")
    @Qualifier("baseNameBackend")
    String baseNameDev () {
      return "http://localhost:8080";
    }

    @Bean
    @Profile("staging")
    @Qualifier("baseNameBackend")
    String baseNameStaging() {
        return "https://dancer.dancier.net";
    }

}
