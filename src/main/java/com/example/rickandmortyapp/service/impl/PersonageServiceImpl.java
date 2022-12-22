package com.example.rickandmortyapp.service.impl;

import com.example.rickandmortyapp.dto.PersonageResponseDto;
import com.example.rickandmortyapp.dto.external.ApiPersonageDto;
import com.example.rickandmortyapp.dto.external.ApiResponsePersonagesDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.exception.DataProcessingException;
import com.example.rickandmortyapp.model.ExternalLink;
import com.example.rickandmortyapp.model.Personage;
import com.example.rickandmortyapp.repository.ExternalLinkRepository;
import com.example.rickandmortyapp.repository.PersonageRepository;
import com.example.rickandmortyapp.service.HttpClient;
import com.example.rickandmortyapp.service.PersonageService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@Service
public class PersonageServiceImpl implements PersonageService {
    private final HttpClient httpClient;
    private final PersonageRepository personageRepository;
    private final ExternalLinkRepository externalLinkRepository;
    private final ResponseMapper<PersonageResponseDto, ApiPersonageDto, Personage> personageMapper;

    public PersonageServiceImpl(
            HttpClient httpClient, PersonageRepository personageRepository,
            ExternalLinkRepository externalLinkRepository,
            ResponseMapper<PersonageResponseDto, ApiPersonageDto, Personage> personageMapper) {
        this.httpClient = httpClient;
        this.personageRepository = personageRepository;
        this.externalLinkRepository = externalLinkRepository;
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
        Map<Long, ApiPersonageDto> externalsDtos = Arrays
                .stream(apiResponsePersonagesDto.getResults())
                .collect(Collectors.toMap(ApiPersonageDto::getId, Function.identity()));
        Set<Long> externalIds = externalsDtos.keySet();

        List<Personage> existingPersonages = personageRepository
                .findAllByExternalIdIn(externalIds);
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

        Map<Long, ExternalLink> oldPersonageEpisodes = oldPersonage.getEpisodes().stream()
                .collect(Collectors.toMap(ExternalLink::getExternalId, Function.identity()));
        Map<Long, ExternalLink> newPersonageEpisodes = newPersonage.getEpisodes().stream()
                .collect(Collectors.toMap(ExternalLink::getExternalId, Function.identity()));

        Set<Long> idsToDelete = Set.copyOf(oldPersonageEpisodes.keySet().stream()
                .filter(k -> newPersonageEpisodes.get(k) == null)
                .collect(Collectors.toList()));
        if (idsToDelete.size() > 0) {
            externalLinkRepository.deleteAllByParentIdAndExternalIdIn(oldPersonage.getId(),
                    idsToDelete);
        }

        List<ExternalLink> externalLinksToUpdate = newPersonage.getEpisodes().stream()
                .peek(e -> e.setId(oldPersonageEpisodes.get(e.getExternalId()).getId()))
                .collect(Collectors.toList());
        oldPersonage.setEpisodes(externalLinksToUpdate);

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
