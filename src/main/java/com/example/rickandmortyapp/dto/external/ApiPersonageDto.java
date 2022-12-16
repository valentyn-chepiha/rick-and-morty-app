package com.example.rickandmortyapp.dto.external;

import lombok.Data;

@Data
public class ApiPersonageDto {
    private Long id;
    private String name;
    private String status;
    private String specie;
    private String type;
    private String gender;
    private String image;
    private String[] episode;
    private ApiPersonageOriginDto origin;
    private ApiPersonageLocationDto location;
}
