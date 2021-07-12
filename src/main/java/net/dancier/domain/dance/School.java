package net.dancier.domain.dance;

import lombok.Builder;
import lombok.Data;
import net.dancier.domain.Recommendable;
import net.dancier.domain.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URL;

/**
 * A dancing school.
 */
@Data
@Builder
public class School implements Recommendable {

    @NotNull
    private User owner;

    @NotEmpty
    private String name;

    private URL website;

}
