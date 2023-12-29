package ar.juarce.models;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    MODERATOR("ROLE_MODERATOR"),
    USER("ROLE_USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

}
