package net.dancier.dancer.core.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "dancer")
public class Dancer implements Recommendable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private Integer version;

    private UUID userId;

    private String dancerName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "able_to",
            joinColumns = @JoinColumn(name = "dancer_id"),
            inverseJoinColumns = @JoinColumn(name = "dance_profile_id"))
    private Set<DanceProfile> ableTo = new HashSet<>();

    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinTable(name = "wants_to",
            joinColumns = @JoinColumn(name = "dancer_id"),
            inverseJoinColumns = @JoinColumn(name = "dance_profile_id"))
    private Set<DanceProfile> wantsTo = new HashSet<>();

    private Integer size;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String zipCode;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String city;

    private Double latitude;

    private Double longitude;

    private String profileImageHash;

    private String aboutMe;

    private Instant updatedAt;


}
