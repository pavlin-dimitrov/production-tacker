package com.example.productiontracker.enums;

public enum Role {
    USER, ADMIN;

    public String authority() {
        return this.name();
    }
}
