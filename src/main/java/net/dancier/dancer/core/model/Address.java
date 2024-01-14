package net.dancier.dancer.core.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String street;
    private String zipCode;
    private String city;

}
