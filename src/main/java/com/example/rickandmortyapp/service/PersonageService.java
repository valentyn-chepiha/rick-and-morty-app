package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.model.Personage;

public interface PersonageService extends ExternalDataService {
    Personage getRandomPersonage();
}
