package com.example.rickandmortyapp.repository;

import java.util.List;
import java.util.Set;
import com.example.rickandmortyapp.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findAllByExternalIdIn(Set<Long> externalIds);

    @Query(value = "select e.external_link_id from episodes_external_links e where e.episode_id in (?1)",
            nativeQuery = true)
    List<Long> findAllIdExternalLinksByIdIn(Set<Long> externalIds);
}
