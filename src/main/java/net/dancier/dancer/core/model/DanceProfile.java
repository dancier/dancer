package net.dancier.dancer.core.model;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import jakarta.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "dance_profile")
public class DanceProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "dance_id", referencedColumnName = "id")
    private Dance dance;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    @Column(name = "leader")
    private Leading leading;

}
