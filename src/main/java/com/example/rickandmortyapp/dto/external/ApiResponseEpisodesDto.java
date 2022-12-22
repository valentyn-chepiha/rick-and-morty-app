package com.example.rickandmortyapp.dto.external;

import lombok.Data;

@Data
public class ApiResponseEpisodesDto {
    private ApiInfoDto info;
    private ApiEpisodeDto[] results;
}
