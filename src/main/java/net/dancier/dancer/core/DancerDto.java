package net.dancier.dancer.core;

import lombok.Data;
import net.dancier.dancer.core.model.Sex;

import java.util.Set;
import java.util.UUID;

@Data
public class DancerDto {

    private UUID id;

    private Set<DanceProfileDto> ableTo;

    private Set<DanceProfileDto> wantsTo;

    private int size;

    private Sex sex;
}
