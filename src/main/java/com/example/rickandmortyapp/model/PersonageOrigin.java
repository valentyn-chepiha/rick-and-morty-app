package com.example.rickandmortyapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "personage_origins")
public class PersonageOrigin {
    @Id
    @Column(name = "personage_id")
    private Long id;
    private String name;
    @Column(name = "external_id")
    private Long eternalId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "personage_id")
    private Personage personage;
}
