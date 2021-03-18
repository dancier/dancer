package net.dancier.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.dancier.domain.dance.Ambition;
import net.dancier.domain.dance.Capability;
import net.dancier.domain.dance.Smoker;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class Profile {

    private UUID userId;
    private String userName;
    private UUID imageId;
    private Integer size;
    private Smoker smoker;
    private String aboutHim;
    private Date birthDate;
    private String email;

    private Set<Capability> canDo;
    private Set<Capability> wants;

    private Set<Ambition> ambitions;

}
