package com.example.rickandmortyapp.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.example.rickandmortyapp.dto.PersonageResponseDto;
import com.example.rickandmortyapp.dto.external.ApiPersonageDto;
import com.example.rickandmortyapp.dto.external.ApiResponsePersonagesDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.exception.DataProcessingException;
import com.example.rickandmortyapp.model.Personage;
import com.example.rickandmortyapp.repository.PersonageLocationRepository;
import com.example.rickandmortyapp.repository.PersonageOriginRepository;
import com.example.rickandmortyapp.repository.PersonageRepository;
import com.example.rickandmortyapp.service.HttpClient;
import com.example.rickandmortyapp.service.PersonageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@Service
public class PersonageServiceImpl implements PersonageService {
    private final HttpClient httpClient;
    private final PersonageRepository personageRepository;
    private final PersonageOriginRepository personageOriginRepository;
    private final PersonageLocationRepository personageLocationRepository;
    private final ResponseMapper<PersonageResponseDto, ApiPersonageDto, Personage> personageMapper;

    public PersonageServiceImpl(HttpClient httpClient, PersonageRepository personageRepository,
                                PersonageOriginRepository personageOriginRepository,
                                PersonageLocationRepository personageLocationRepository,
                                ResponseMapper<PersonageResponseDto, ApiPersonageDto, Personage> personageMapper) {
        this.httpClient = httpClient;
        this.personageRepository = personageRepository;
        this.personageOriginRepository = personageOriginRepository;
        this.personageLocationRepository = personageLocationRepository;
        this.personageMapper = personageMapper;
    }

    @Override
    public void syncExternalPersonages() {
        log.info("PersonageServiceImpl.syncExternalPersonages started...");
        ApiResponsePersonagesDto apiResponsePersonagesDto = null;
        do {
            String url = apiResponsePersonagesDto == null
                    ? "https://rickandmortyapi.com/api/character"
                    : apiResponsePersonagesDto.getInfo().getNext();
            apiResponsePersonagesDto = httpClient.get(url, ApiResponsePersonagesDto.class);
            saveDtpToDb(apiResponsePersonagesDto);
        } while (apiResponsePersonagesDto.getInfo().getNext() != null);
        log.info("PersonageServiceImpl.syncExternalPersonages done!");
    }

    @Override
    public Personage getRandomPersonage() {
        long countPersonages = personageRepository.count();
        long id = (long) (Math.random() * countPersonages);
        return personageRepository.findById(id).orElseThrow(() -> new
                DataProcessingException("Personage not found by id: " + id));
    }

    private void saveDtpToDb(ApiResponsePersonagesDto apiResponsePersonagesDto) {
        Map<Long, ApiPersonageDto> externalsDtos = Arrays.stream(apiResponsePersonagesDto.getResults())
                .collect(Collectors.toMap(ApiPersonageDto::getId, Function.identity()));
        Set<Long> externalIds = externalsDtos.keySet();

        List<Personage> existingPersonages = personageRepository.findAllByExternalIdIn(externalIds);
        Map<Long, Personage> existingPersonagesWithIds = existingPersonages.stream()
                .collect(Collectors.toMap(Personage::getExternalId, Function.identity()));
        Set<Long> existingIds = existingPersonagesWithIds.keySet();

        List<Personage> personagesToUpdate = existingPersonages.stream()
                .map(p -> updateFieldPersonage(p, externalsDtos.get(p.getExternalId())))
                .collect(Collectors.toList());
        personageRepository.saveAll(personagesToUpdate);

        externalIds.removeAll(existingIds);
        List<Personage> personagesToSave = externalIds.stream()
                .map(id -> personageMapper.parseApiEntityResponseDto(externalsDtos.get(id)))
                .collect(Collectors.toList());
        personageRepository.saveAll(personagesToSave);
    }

    private Personage updateFieldPersonage(Personage oldPersonage, ApiPersonageDto dto) {
        Personage newPersonage = personageMapper.parseApiEntityResponseDto(dto);
        oldPersonage.setType(newPersonage.getType());
        oldPersonage.setStatus(newPersonage.getStatus());
        oldPersonage.setSpecie(newPersonage.getSpecie());
        oldPersonage.setGender(newPersonage.getGender());
        oldPersonage.setName(newPersonage.getName());
        oldPersonage.setImage(newPersonage.getImage());
// todo
//      change update external_links
        oldPersonage.setEpisodes(newPersonage.getEpisodes());

        if (newPersonage.getOrigin() == null) {
            oldPersonage.setOrigin(null);
        } else {
            oldPersonage.getOrigin().setName(newPersonage.getOrigin().getName());
            oldPersonage.getOrigin().setEternalId(newPersonage.getOrigin().getEternalId());
        }

        if (newPersonage.getLocation() == null) {
            oldPersonage.setLocation(null);
        } else {
            oldPersonage.getLocation().setName(newPersonage.getLocation().getName());
            oldPersonage.getLocation().setEternalId(newPersonage.getLocation().getEternalId());
        }
        return oldPersonage;
    }
}
