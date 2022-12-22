package com.example.rickandmortyapp.repository;

import com.example.rickandmortyapp.model.ExternalLink;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExternalLinkRepository extends JpaRepository<ExternalLink, Long> {
    @Query(value = "delete from external_links e "
            + "where e.id in (select pe.external_link_id from personages_external_links pe "
            + "where pe.personage_id = ?1) and e.external_id in (?2)",
            nativeQuery = true)
    void deleteAllByParentIdAndExternalIdIn(Long id, Set<Long> externalIds);
}
