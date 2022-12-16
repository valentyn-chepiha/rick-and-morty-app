package com.example.rickandmortyapp.dto.mapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.example.rickandmortyapp.dto.ExternalLinkResponseDto;
import com.example.rickandmortyapp.dto.LocationResponseDto;
import com.example.rickandmortyapp.dto.external.ApiExternalLinkDto;
import com.example.rickandmortyapp.dto.external.ApiLocationDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.ExternalLink;
import com.example.rickandmortyapp.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper implements ResponseMapper<LocationResponseDto, ApiLocationDto, Location> {
    private final ResponseMapper<ExternalLinkResponseDto, ApiExternalLinkDto, ExternalLink> externalLinkMapper;

    public LocationMapper(ResponseMapper<ExternalLinkResponseDto, ApiExternalLinkDto, ExternalLink> externalLinkMapper) {
        this.externalLinkMapper = externalLinkMapper;
    }

    @Override
    public Location parseApiEntityResponseDto(ApiLocationDto dto) {
        Location location = new Location();
        location.setExternalId(dto.getId());
        location.setName(dto.getName());
        location.setDimension(dto.getDimension());
        location.setType(dto.getType());
        location.setResidents(Arrays.stream(dto.getResidents())
                .map(s -> externalLinkMapper.parseApiEntityResponseDto(new ApiExternalLinkDto(s)))
                .collect(Collectors.toList()));
        return location;
    }

    @Override
    public LocationResponseDto toResponseDto(Location location) {
        LocationResponseDto dto = new LocationResponseDto();
        dto.setId(location.getId());
        dto.setExternalId(location.getExternalId());
        dto.setName(location.getName());
        dto.setDimension(location.getDimension());
        dto.setType(location.getType());
        dto.setResidents(location.getResidents().stream()
                .map(externalLinkMapper::toResponseDto)
                .collect(Collectors.toList()));
        return dto;
    }
}
