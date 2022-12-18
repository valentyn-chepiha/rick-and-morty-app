package com.example.rickandmortyapp.dto;

import java.util.List;
import lombok.Data;

@Data
public class EpisodeResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private String airDate;
    private String episode;
    private String url;
    private String created;
    private List<ExternalLinkResponseDto> characters;
}
