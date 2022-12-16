package com.example.rickandmortyapp.dto.mapper;

public interface ResponseMapper<R, D, E> {
    E parseApiEntityResponseDto(D dto);

    R toResponseDto(E entity);
}
