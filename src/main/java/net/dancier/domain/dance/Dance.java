package net.dancier.domain.dance;

import lombok.Data;

import java.util.UUID;

/**
 * Some dance.
 */
@Data
public class Dance {
    private UUID id;
    private String name;
}
