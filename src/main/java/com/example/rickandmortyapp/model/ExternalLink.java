package com.example.rickandmortyapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "external_links")
public class ExternalLink {
    @Id
    @GeneratedValue(generator = "external_links_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "external_links_id_seq", sequenceName = "external_links_id_seq",
            allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private Long externalId;
}
