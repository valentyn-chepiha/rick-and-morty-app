package com.example.rickandmortyapp.repository;

import java.util.List;
import java.util.Set;
import com.example.rickandmortyapp.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByExternalIdIn(Set<Long> externalIds);

    @Query(value = "select e.external_link_id from locations_external_links e where e.location_id in (?1)",
            nativeQuery = true)
    List<Long> findAllIdExternalLinksByIdIn(Set<Long> ids);
}
