package net.dancier.dancer.chat.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class DancersDto {
    private HashMap<UUID, DancerDto> dancers;
}
