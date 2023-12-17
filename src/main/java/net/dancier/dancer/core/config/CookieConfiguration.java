package net.dancier.dancer.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "app.cookie")
public class CookieConfiguration {
    private Boolean secure;
    private String sameSite;
}
