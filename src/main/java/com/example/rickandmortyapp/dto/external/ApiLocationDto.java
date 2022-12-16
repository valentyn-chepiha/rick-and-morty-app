package com.example.rickandmortyapp.dto.external;

import lombok.Data;

@Data
public class ApiLocationDto {
    private Long id;
    private String name;
    private String type;
    private String dimension;
    private String[] residents;
}
