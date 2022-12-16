package com.example.rickandmortyapp.repository;

import java.util.List;
import java.util.Set;
import com.example.rickandmortyapp.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByExternalId(Long externalId);

    List<Location> findAllByExternalIdIn(Set<Long> externalIds);

    // todo
    //      add residents
}
