package com.example.rickandmortyapp.dto.mapper.impl;

import com.example.rickandmortyapp.dto.ExternalLinkResponseDto;
import com.example.rickandmortyapp.dto.external.ApiExternalLinkDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.ExternalLink;
import org.springframework.stereotype.Component;

@Component
public class ExternalLinkMapper implements ResponseMapper<ExternalLinkResponseDto,
        ApiExternalLinkDto, ExternalLink> {
    @Override
    public ExternalLink parseApiEntityResponseDto(ApiExternalLinkDto dto) {
        ExternalLink externalLink = new ExternalLink();
        String[] link = dto.getLink().split("/");
        externalLink.setExternalId(Long.parseLong(link[link.length - 1]));
        return externalLink;
    }

    @Override
    public ExternalLinkResponseDto toResponseDto(ExternalLink entity) {
        ExternalLinkResponseDto dto = new ExternalLinkResponseDto();
        dto.setId(entity.getId());
        dto.setExternalId(entity.getExternalId());
        return dto;
    }

}
