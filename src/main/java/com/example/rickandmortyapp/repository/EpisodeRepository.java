package com.example.rickandmortyapp.repository;

import com.example.rickandmortyapp.model.Episode;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findAllByExternalIdIn(Set<Long> externalIds);
}
