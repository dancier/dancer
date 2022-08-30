package net.dancier.dancer.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsernameAvailableDto {

    private String name;

    private Boolean available;

}
