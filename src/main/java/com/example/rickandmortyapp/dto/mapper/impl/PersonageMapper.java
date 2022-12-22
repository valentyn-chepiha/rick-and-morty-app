package com.example.rickandmortyapp.dto.mapper.impl;

import com.example.rickandmortyapp.dto.ExternalLinkResponseDto;
import com.example.rickandmortyapp.dto.PersonageLocationResponseDto;
import com.example.rickandmortyapp.dto.PersonageOriginResponseDto;
import com.example.rickandmortyapp.dto.PersonageResponseDto;
import com.example.rickandmortyapp.dto.external.ApiExternalLinkDto;
import com.example.rickandmortyapp.dto.external.ApiPersonageDto;
import com.example.rickandmortyapp.dto.external.ApiPersonageLocationDto;
import com.example.rickandmortyapp.dto.external.ApiPersonageOriginDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.ExternalLink;
import com.example.rickandmortyapp.model.Personage;
import com.example.rickandmortyapp.model.PersonageLocation;
import com.example.rickandmortyapp.model.PersonageOrigin;
import com.example.rickandmortyapp.model.type.TypeGender;
import com.example.rickandmortyapp.model.type.TypeStatus;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PersonageMapper implements
        ResponseMapper<PersonageResponseDto, ApiPersonageDto, Personage> {
    private final ResponseMapper<ExternalLinkResponseDto, ApiExternalLinkDto, ExternalLink>
            externalLinkMapper;
    private final ResponseMapper<PersonageOriginResponseDto, ApiPersonageOriginDto,
            PersonageOrigin> personageOriginMapper;
    private final ResponseMapper<PersonageLocationResponseDto, ApiPersonageLocationDto,
            PersonageLocation> personageLocationMapper;

    public PersonageMapper(
            ResponseMapper<ExternalLinkResponseDto, ApiExternalLinkDto, ExternalLink>
                    externalLinkMapper,
            ResponseMapper<PersonageOriginResponseDto, ApiPersonageOriginDto, PersonageOrigin>
                    personageOriginMapper,
            ResponseMapper<PersonageLocationResponseDto, ApiPersonageLocationDto,
                    PersonageLocation> personageLocationMapper) {
        this.externalLinkMapper = externalLinkMapper;
        this.personageOriginMapper = personageOriginMapper;
        this.personageLocationMapper = personageLocationMapper;
    }

    @Override
    public Personage parseApiEntityResponseDto(ApiPersonageDto dto) {
        Personage personage = new Personage();
        personage.setName(dto.getName());
        personage.setGender(TypeGender.valueOf(dto.getGender().toUpperCase()));
        personage.setSpecie(dto.getSpecie());
        personage.setStatus(TypeStatus.valueOf(dto.getStatus().toUpperCase()));
        personage.setType(dto.getType());
        personage.setExternalId(dto.getId());
        personage.setImage(dto.getImage());
        personage.setEpisodes(Arrays.stream(dto.getEpisode())
                .map(ApiExternalLinkDto::new)
                .map(externalLinkMapper::parseApiEntityResponseDto)
                .collect(Collectors.toList()));
        if (!dto.getOrigin().getUrl().isEmpty()) {
            personage.setOrigin(personageOriginMapper.parseApiEntityResponseDto(dto.getOrigin()));
            personage.getOrigin().setPersonage(personage);
        }
        if (!dto.getLocation().getUrl().isEmpty()) {
            personage.setLocation(personageLocationMapper.parseApiEntityResponseDto(
                    dto.getLocation()));
            personage.getLocation().setPersonage(personage);
        }
        return personage;
    }

    @Override
    public PersonageResponseDto toResponseDto(Personage personage) {
        PersonageResponseDto dto = new PersonageResponseDto();
        dto.setId(personage.getId());
        dto.setExternalId(personage.getExternalId());
        dto.setName(personage.getName());
        dto.setGender(personage.getGender().getName());
        dto.setSpecie(personage.getSpecie());
        dto.setStatus(personage.getStatus().getName());
        dto.setType(personage.getType());
        dto.setImage(personage.getImage());
        dto.setEpisodes(personage.getEpisodes().stream()
                .map(externalLinkMapper::toResponseDto)
                .collect(Collectors.toList()));
        dto.setOrigin(personageOriginMapper.toResponseDto(personage.getOrigin()));
        dto.setLocation(personageLocationMapper.toResponseDto(personage.getLocation()));
        return dto;
    }
}
