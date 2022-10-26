package net.dancier.dancer.chat.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class DancerIdsDto {
    private Set<UUID> dancerIds;
}
