package com.example.rickandmortyapp.repository;

import com.example.rickandmortyapp.model.Location;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value = "select distinct l from Location l left join fetch l.residents where l.externalId in (:externalIds)")
    List<Location> findAllByExternalIdIn(Set<Long> externalIds);
}
