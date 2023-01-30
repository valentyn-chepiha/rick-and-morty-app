package com.example.rickandmortyapp.repository;

import com.example.rickandmortyapp.model.Personage;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonageRepository extends JpaRepository<Personage, Long> {
    @Query(value = "from Personage p left join fetch p.episodes where p.id=:id")
    Personage getWithEpisodesById(Long id);

    @Query(value = "from Personage p left join fetch p.episodes where p.id=:id")
    Optional<Personage> findWithEpisodesById(Long id);

    @Query(value = "select distinct p from Personage p left join fetch p.episodes where p.externalId in (:externalIds)")
    List<Personage> findAllByExternalIdIn(Set<Long> externalIds);

    @Query(value = "select p.*, l.* from personages p left join personages_external_links l on p.id = l.personage_id where UPPER(p.name) like UPPER('%' || ?1 || '%')",
            nativeQuery = true)
    List<Personage> findAllByNameContainsNoRegister(String name);
}
