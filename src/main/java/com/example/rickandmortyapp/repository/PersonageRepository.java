package com.example.rickandmortyapp.repository;

import java.util.List;
import java.util.Set;
import com.example.rickandmortyapp.model.Personage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonageRepository extends JpaRepository<Personage, Long> {
    List<Personage> findAllByExternalIdIn(Set<Long> externalIds);

    Personage findByExternalId(Long externalId);

    @Query(value = "select p.* from personages p where UPPER(p.name) like UPPER('%' || ?1 || '%')",
            nativeQuery = true)
    List<Personage> findAllByNameContainsNoRegister(String name);

    // todo
    //      add episodes
}