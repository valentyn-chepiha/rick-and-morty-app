package com.example.rickandmortyapp.repository;

import java.util.Set;
import com.example.rickandmortyapp.model.PersonageLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonageLocationRepository extends JpaRepository<PersonageLocation, Long> {
    void deleteAllByPersonageIdIn(Set<Long> id);
}
