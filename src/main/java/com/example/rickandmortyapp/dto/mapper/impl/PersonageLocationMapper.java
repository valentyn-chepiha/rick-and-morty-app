package com.example.rickandmortyapp.dto.mapper.impl;

import com.example.rickandmortyapp.dto.PersonageLocationResponseDto;
import com.example.rickandmortyapp.dto.external.ApiPersonageLocationDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.PersonageLocation;
import org.springframework.stereotype.Component;

@Component
public class PersonageLocationMapper implements ResponseMapper<PersonageLocationResponseDto,
        ApiPersonageLocationDto, PersonageLocation> {
    @Override
    public PersonageLocation parseApiEntityResponseDto(ApiPersonageLocationDto dto) {
        PersonageLocation personageLocation = new PersonageLocation();
        String[] url = dto.getUrl().split("/");
        personageLocation.setEternalId(Long.parseLong(url[url.length - 1]));
        personageLocation.setName(dto.getName());
        return personageLocation;
    }

    @Override
    public PersonageLocationResponseDto toResponseDto(PersonageLocation entity) {
        PersonageLocationResponseDto dto = new PersonageLocationResponseDto();
        dto.setId(entity.getId());
        dto.setEternalId(entity.getEternalId());
        dto.setName(entity.getName());
        return dto;
    }
}
