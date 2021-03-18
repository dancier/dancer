package net.dancier.db;

import lombok.Data;
import net.dancier.domain.dance.Smoker;

import java.util.Date;
import java.util.UUID;

@Data
public class DancerDto {
    UUID userId;
    String userName;
    UUID imageId;
    Integer size;
    String aboutHim;
    Date birthDate;
    Smoker smoker;
}
