package com.example.rickandmortyapp.repository;

import java.util.Set;
import com.example.rickandmortyapp.model.PersonageOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonageOriginRepository extends JpaRepository<PersonageOrigin, Long> {
    void deleteAllByPersonageIdIn(Set<Long> id);
}
