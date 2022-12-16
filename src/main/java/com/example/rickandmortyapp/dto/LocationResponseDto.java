package com.example.rickandmortyapp.dto;

import java.util.List;
import lombok.Data;

@Data
public class LocationResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private String type;
    private String dimension;
    private List<ExternalLinkResponseDto> residents;
}
