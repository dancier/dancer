package net.dancier.dancer.mail.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public class EntityWithUUID {
    @Id
    private UUID id;

    public EntityWithUUID() {
        this.id = UUID.randomUUID();
    }
}