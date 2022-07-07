package net.dancier.dancer.cabaceo;

import lombok.Data;

import java.util.UUID;

@Data
public class NewCabaceoDto {
    private UUID targetDancer;
    private String message;
    
}
