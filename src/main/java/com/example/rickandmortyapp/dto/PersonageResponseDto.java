package com.example.rickandmortyapp.dto;

import java.util.List;
import lombok.Data;

@Data
public class PersonageResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private String status;
    private String specie;
    private String type;
    private String gender;
    private String image;
    private List<ExternalLinkResponseDto> episodes;
    private PersonageOriginResponseDto origin;
    private PersonageLocationResponseDto location;
}
