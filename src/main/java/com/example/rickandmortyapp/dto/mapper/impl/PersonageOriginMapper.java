package com.example.rickandmortyapp.dto.mapper.impl;

import com.example.rickandmortyapp.dto.PersonageOriginResponseDto;
import com.example.rickandmortyapp.dto.external.ApiPersonageOriginDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.PersonageOrigin;
import org.springframework.stereotype.Component;

@Component
public class PersonageOriginMapper implements ResponseMapper<PersonageOriginResponseDto,
        ApiPersonageOriginDto, PersonageOrigin> {
    @Override
    public PersonageOrigin parseApiEntityResponseDto(ApiPersonageOriginDto dto) {
        PersonageOrigin personageOrigin = new PersonageOrigin();
        String[] urlParams = dto.getUrl().split("/");
        personageOrigin.setEternalId(Long.parseLong(urlParams[urlParams.length - 1]));
        personageOrigin.setName(dto.getName());
        return personageOrigin;
    }

    @Override
    public PersonageOriginResponseDto toResponseDto(PersonageOrigin entity) {
        PersonageOriginResponseDto dto = new PersonageOriginResponseDto();
        dto.setId(entity.getId());
        dto.setEternalId(entity.getEternalId());
        dto.setName(entity.getName());
        return dto;
    }
}
