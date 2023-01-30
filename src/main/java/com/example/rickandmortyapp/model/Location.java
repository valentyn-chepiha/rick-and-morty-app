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
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(generator = "locations_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "locations_id_seq", sequenceName = "locations_id_seq",
            allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private Long externalId;
    private String name;
    private String type;
    private String dimension;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "locations_external_links",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "external_link_id"))
    private List<ExternalLink> residents;
}
