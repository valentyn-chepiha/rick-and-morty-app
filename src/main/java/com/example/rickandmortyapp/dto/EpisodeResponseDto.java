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

/* todo
        add fields
{
    "id":1,
    "name":"Pilot",
    "air_date":"December 2, 2013",
    "episode":"S01E01",
    "characters":[
        "https://rickandmortyapi.com/api/character/1",
        "https://rickandmortyapi.com/api/character/2",
        ...
        "https://rickandmortyapi.com/api/character/435"
        ],
    "url":"https://rickandmortyapi.com/api/episode/1",
    "created":"2017-11-10T12:56:33.798Z"}
* */

