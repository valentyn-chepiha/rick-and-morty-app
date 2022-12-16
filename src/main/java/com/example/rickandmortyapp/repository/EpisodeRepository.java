package com.example.rickandmortyapp.repository;

import java.util.List;
import java.util.Set;
import com.example.rickandmortyapp.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    Episode findByExternalId(Long externalId);

    List<Episode> findAllByExternalIdIn(Set<Long> externalIds);
    // todo
    //      add characters
}
