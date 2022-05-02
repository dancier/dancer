package net.dancier.dancer.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class BaseNameConfiguration {

    @Bean(name = "backendBaseName")
    @Profile("dev")
    String baseNameBackendDev() {
      return "http://localhost:8080";
    }

    @Bean(name = "backendBaseName")
    @Profile("staging")
    String baseNameBackendStaging() {
        return "https://dancer.dancier.net";
    }

    @Bean(name ="frontendBaseName")
    @Profile("dev")
    String baseNameFrontendDev() {
        return "http://localhost:4200";
    }

    @Bean(name = "frontendBaseName")
    @Profile("staging")
    String baseNameFrontendStaging() {
        return "https://dancier.net";
    }

}
