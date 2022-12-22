package com.example.rickandmortyapp.model.type;

public enum TypeStatus {
    ALIVE("Alive"), DEAD("Dead"), UNKNOWN("Unknown");

    private String name;

    TypeStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
