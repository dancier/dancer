package net.dancier.dancer.school;

import lombok.Data;
import net.dancier.dancer.core.model.Country;
import net.dancier.dancer.core.model.Dance;
import net.dancier.dancer.core.model.Recommendable;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class School implements Recommendable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    private String name;

    private String url;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "school_dances",
            joinColumns = @JoinColumn(name = "school_id"),
            inverseJoinColumns = @JoinColumn(name ="dance_id")
    )
    private Set<Dance> supportedDances = Set.of();

    @Enumerated(EnumType.STRING)
    private Country country;

    private String zipCode;

    private String city;

    private Double latitude;

    private Double longitude;

    private String profileImageHash;

}
