package net.dancier.domain.dance;

import lombok.Data;
import net.dancier.domain.Recommendable;
import net.dancier.domain.User;

import java.net.URL;

@Data
public class School implements Recommendable {
    private User user;

    private String name;

    private URL website;

}
