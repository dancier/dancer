package net.dancier.dancer.chat.dto;

import lombok.Data;
import net.dancier.dancer.core.model.Dancer;

import java.util.UUID;

@Data
public class DancerDto {
    private UUID id;
    private String dancerName;
    private String city;
    private String profileImageHash;

    public static DancerDto fromDancer(Dancer dancer) {
        DancerDto dancerDto = new DancerDto();
        dancerDto.id = dancer.getId();
        dancerDto.dancerName = dancer.getDancerName();
        dancerDto.city = dancer.getCity();
        dancerDto.profileImageHash = dancer.getProfileImageHash();
        return dancerDto;
    }
}
