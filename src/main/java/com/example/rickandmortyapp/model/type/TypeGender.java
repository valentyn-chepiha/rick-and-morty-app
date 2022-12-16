package com.example.rickandmortyapp.model.type;

public enum TypeGender {
    FEMALE("Female"), MALE("Male"), GENDERLESS("Genderless"), UNKNOWN("Unknown");

    private String name;

    TypeGender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
