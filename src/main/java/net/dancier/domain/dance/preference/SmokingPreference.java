package net.dancier.domain.dance.preference;

import lombok.Getter;
import lombok.Setter;

/**
 * Preference on smoking behaviour of the dance partner.
 */
public class SmokingPreference extends Preference {
    /**
     * {@literal true} if a smoker is preferred, {@literal false} if a smoker is unwanted.
     */
    @Getter
    @Setter
    boolean smoke;

    @Override
    public String getName() {
        return "Smoking";
    }
}
