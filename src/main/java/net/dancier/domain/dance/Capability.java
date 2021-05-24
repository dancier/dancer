package net.dancier.domain.dance;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * A dance capability. Specifies a dance and the level.
 */
@Data
public class Capability {
    @NotNull
    private Dance dance;
    @NotNull
    private Level level;
}
