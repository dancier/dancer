package net.dancier.domain.dance;

import lombok.Builder;
import lombok.Data;
import net.dancier.domain.Image;
import net.dancier.domain.Recommendable;
import net.dancier.domain.User;
import net.dancier.domain.dance.preference.Preference;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Builder
public class Dancer implements Recommendable {

    private User user;

    @NotNull
    private Gender gender;

    private String userName;
    private Image image;
    private Integer size;
    private Date birth;

    @NotNull
    private SmokingBehaviour smoker;

    /**
     * Some information about the dancer.
     */
    private String about;

    /**
     * Capabilities the dancer has.
     */
    private Set<Capability> capability;

    /**
     * Dance ambitions of the dancer.
     */
    private Set<Ambition> ambitions;
    /**
     * Preferences except ambitions.
     */
    private Set<Preference> preferences;

}
