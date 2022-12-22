package com.example.rickandmortyapp.dto.mapper.impl;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.example.rickandmortyapp.dto.EpisodeResponseDto;
import com.example.rickandmortyapp.dto.ExternalLinkResponseDto;
import com.example.rickandmortyapp.dto.external.ApiEpisodeDto;
import com.example.rickandmortyapp.dto.external.ApiExternalLinkDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.Episode;
import com.example.rickandmortyapp.model.ExternalLink;
import org.springframework.stereotype.Component;

@Component
public class EpisodeMapper implements ResponseMapper<EpisodeResponseDto, ApiEpisodeDto, Episode> {
    private final ResponseMapper<ExternalLinkResponseDto, ApiExternalLinkDto, ExternalLink> externalLinkMapper;

    public EpisodeMapper(ResponseMapper<ExternalLinkResponseDto, ApiExternalLinkDto, ExternalLink> externalLinkMapper) {
        this.externalLinkMapper = externalLinkMapper;
    }

    @Override
    public Episode parseApiEntityResponseDto(ApiEpisodeDto dto) {
        Episode episode = new Episode();
        episode.setName(dto.getName());
        episode.setAirDate(dto.getAirDate());
        episode.setEpisode(dto.getEpisode());
        episode.setExternalId(dto.getId());
        episode.setCharacters(Arrays.stream(dto.getCharacters())
                .map(ApiExternalLinkDto::new)
                .map(externalLinkMapper::parseApiEntityResponseDto)
                .collect(Collectors.toList()));
        return episode;
    }

    @Override
    public EpisodeResponseDto toResponseDto(Episode episode) {
        EpisodeResponseDto dto = new EpisodeResponseDto();
        dto.setId(episode.getId());
        dto.setExternalId(episode.getExternalId());
        dto.setName(episode.getName());
        dto.setAirDate(episode.getAirDate());
        dto.setEpisode(episode.getEpisode());
        dto.setCharacters(episode.getCharacters().stream()
                .map(externalLinkMapper::toResponseDto)
                .collect(Collectors.toList()));
        return dto;
    }
}
