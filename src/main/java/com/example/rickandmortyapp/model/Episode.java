package com.example.rickandmortyapp.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(generator = "episodes_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "episodes_id_seq", sequenceName = "episodes_id_seq",
            allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private Long externalId;
    private String name;
    @Column(name = "air_date")
    private String airDate;
    private String episode;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "episodes_external_links",
            joinColumns = @JoinColumn(name = "episode_id"),
            inverseJoinColumns = @JoinColumn(name = "external_link_id"))
    private List<ExternalLink> characters;
}
