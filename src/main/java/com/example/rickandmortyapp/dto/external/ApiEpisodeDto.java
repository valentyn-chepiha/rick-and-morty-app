package com.example.rickandmortyapp.dto.external;

import lombok.Data;

@Data
public class ApiEpisodeDto {
    private Long id;
    private String name;
    private String airDate;
    private String episode;
    private String url;
    private String created;
    private String[] characters;
}
