package com.example.rickandmortyapp.repository;

import java.util.Set;
import com.example.rickandmortyapp.model.ExternalLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalLinkRepository extends JpaRepository<ExternalLink, Long> {
    void deleteAllByIdIn(Set<Long> ids);
}
