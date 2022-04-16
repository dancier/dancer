package net.dancier.dancer.images;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.file")
@Data
public class ImageProperties {

    private String dir;

}
