package com.example.rickandmortyapp.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.example.rickandmortyapp.model.type.TypeGender;
import com.example.rickandmortyapp.model.type.TypeStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "personages")
public class Personage {
    @Id
    @GeneratedValue(generator = "personages_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "personages_id_seq", sequenceName = "personages_id_seq",
            allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private Long externalId;
    private String name;
    @Enumerated(EnumType.STRING)
    private TypeStatus status;
    private String specie;
    private String type;
    @Enumerated(EnumType.STRING)
    private TypeGender gender;
    private String image;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "personages_external_links",
            joinColumns = @JoinColumn(name = "personage_id"),
            inverseJoinColumns = @JoinColumn(name = "external_link_id"))
    private List<ExternalLink> episodes;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personage")
    @PrimaryKeyJoinColumn
    private PersonageOrigin origin;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personage")
    @PrimaryKeyJoinColumn
    private PersonageLocation location;
}
