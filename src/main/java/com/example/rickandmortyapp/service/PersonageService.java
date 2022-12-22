package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.model.Personage;

public interface PersonageService {
    void syncExternalPersonages();

    Personage getRandomPersonage();
}
