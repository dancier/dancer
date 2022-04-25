package net.dancier.dancer.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app.cors")
@Data
@Configuration
public class CorsConfiguration {

    private String[] allowedOrigins;

}
