package com.sroyc.rex.example.common;

public enum UserRole {

    ADMIN("ADMIN");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
