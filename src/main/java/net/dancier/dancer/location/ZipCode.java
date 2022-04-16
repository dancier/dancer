package net.dancier.dancer.location;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "location_zip_code")
public class ZipCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String zipCode;

    private String country;

    private Double longitude;

    private Double latitude;

    private String city;
}
