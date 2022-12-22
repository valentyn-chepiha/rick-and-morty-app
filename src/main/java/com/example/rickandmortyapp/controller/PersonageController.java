package com.example.rickandmortyapp.controller;

import java.util.List;
import java.util.stream.Collectors;
import com.example.rickandmortyapp.dto.PersonageResponseDto;
import com.example.rickandmortyapp.dto.mapper.impl.PersonageMapper;
import com.example.rickandmortyapp.repository.PersonageRepository;
import com.example.rickandmortyapp.service.PersonageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personage")
public class PersonageController {
    private final PersonageRepository personageRepository;
    private final PersonageService personageService;
    private final PersonageMapper personageMapper;

    public PersonageController(PersonageRepository personageRepository,
                               PersonageService personageService,
                               PersonageMapper personageMapper) {
        this.personageRepository = personageRepository;
        this.personageService = personageService;
        this.personageMapper = personageMapper;
    }

    @GetMapping("/random")
    public PersonageResponseDto getRandomPersonage() {
        return personageMapper.toResponseDto(personageService.getRandomPersonage());
    }

    @GetMapping("/by-name")
    public List<PersonageResponseDto> getListPersonagesLikeSubstring(@RequestParam(name = "name") String namePart) {
        return personageRepository.findAllByNameContainsNoRegister(namePart).stream()
                .map(personageMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
