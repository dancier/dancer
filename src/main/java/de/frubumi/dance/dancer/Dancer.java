package de.frubumi.dance.dancer;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Dancer {

    private @Id @GeneratedValue Long id;
    private String firstName;
    private String lastName;
    private String publicName;
    private String dance;

    public Dancer() {}

    public Dancer(final String firstName,
                  final String lastName,
                  final String publicName,
                  final String dance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.publicName = publicName;
        this.dance = dance;
    }
}
