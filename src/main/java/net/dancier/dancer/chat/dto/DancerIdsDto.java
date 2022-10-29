package net.dancier.dancer.chat.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DancerIdsDto {
    private List<UUID> dancerIds;
}
