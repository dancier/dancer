package net.dancier.dancer.core.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "dance")
@Builder
public class Dance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String name;

}
