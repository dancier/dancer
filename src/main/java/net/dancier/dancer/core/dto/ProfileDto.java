package net.dancier.dancer.core.dto;

import lombok.Data;
import net.dancier.dancer.core.model.Sex;

import java.util.Set;
import java.util.UUID;

@Data
public class ProfileDto {

    private UUID id;

    private Set<DanceProfileDto> ableTo;

    private Set<DanceProfileDto> wantsTo;

    private Integer size;

    private Sex sex;

    private Integer age;

    private String userName;

    private String email;

}
