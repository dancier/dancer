package net.dancier.domain.dance;

import lombok.Builder;
import lombok.Data;
import net.dancier.domain.Recommendable;
import net.dancier.domain.User;

import java.net.URL;

@Data
@Builder
public class School implements Recommendable {
    private User user;

    private String name;

    private URL website;

}
