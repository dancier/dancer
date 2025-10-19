package net.dancier.dancer.core.dto;

import lombok.Data;
import net.dancier.dancer.core.model.Gender;

@Data
public class DancerSearchRequestDto {
    private Gender gender;
    private int range = 20;
}
