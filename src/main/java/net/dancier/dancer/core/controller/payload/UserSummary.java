package net.dancier.dancer.core.controller.payload;

import java.util.UUID;

public class UserSummary {
    private UUID id;
    private String username;
    private String name;

    public UserSummary(UUID id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
