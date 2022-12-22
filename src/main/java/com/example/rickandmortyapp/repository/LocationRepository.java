package com.example.rickandmortyapp.repository;

import com.example.rickandmortyapp.model.Location;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByExternalIdIn(Set<Long> externalIds);
}
