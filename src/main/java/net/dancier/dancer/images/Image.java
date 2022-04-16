package net.dancier.dancer.images;

import lombok.Data;

import javax.persistence.Entity;
import java.util.UUID;

@Data
public class Image {

    private String md5sum;

    private UUID owner;


}
