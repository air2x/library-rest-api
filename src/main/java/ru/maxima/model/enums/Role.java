package ru.maxima.model.enums;

public enum Role {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
