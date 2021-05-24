package net.dancier.domain.dance.preference;

import lombok.Getter;
import lombok.Setter;
import net.dancier.domain.dance.Ambition;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * A dancer's preference. {@link Ambition}s are not part of this.
 */
@Getter
@Setter
public abstract class Preference {
    /**
     * Technical key.
     */
    @NotNull
    private UUID id;
    /**
     * Unique name.
     */
    @NotNull
    public abstract String getName();
}
