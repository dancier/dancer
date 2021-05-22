package net.dancier.domain.dance;

import lombok.Builder;
import lombok.Data;
import net.dancier.domain.Image;
import net.dancier.domain.Recommendable;
import net.dancier.domain.User;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class Dancer implements Recommendable {
    private User user;

    private String userName;
    private Image image;
    private Integer size;
    private Date birth;
    private Smoker smoker;

    private String aboutHim;

    private Set<Capability> canDo;
    private Set<Capability> wants;

    private Set<Ambition> ambitions;
}
