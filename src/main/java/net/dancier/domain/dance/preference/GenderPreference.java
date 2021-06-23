package net.dancier.domain.dance.preference;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.dancier.domain.dance.Gender;

/**
 * Preference for a specific gender.
 */
public class GenderPreference extends Preference {

    @Getter
    @Setter
    @NonNull
    private Gender gender;

    @Override
    public String getName() {
        return "Gender";
    }
}
