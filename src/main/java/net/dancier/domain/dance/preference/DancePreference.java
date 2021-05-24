package net.dancier.domain.dance.preference;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.dancier.domain.dance.Dance;

/**
 * Preference for a specific dance.
 */
public class DancePreference extends Preference {

    @Getter
    @Setter
    @NonNull
    private Dance dance;

    @Override
    public String getName() {
        return "Dance";
    }
}
