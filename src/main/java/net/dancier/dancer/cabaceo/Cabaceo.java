package net.dancier.dancer.cabaceo;

import lombok.Data;

import java.util.UUID;

@Data
public class Cabaceo {

    UUID from;
    UUID to;
    String message;

}
