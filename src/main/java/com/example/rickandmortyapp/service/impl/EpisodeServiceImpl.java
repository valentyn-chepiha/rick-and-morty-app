package com.example.rickandmortyapp.service.impl;

import com.example.rickandmortyapp.dto.EpisodeResponseDto;
import com.example.rickandmortyapp.dto.external.ApiEpisodeDto;
import com.example.rickandmortyapp.dto.external.ApiResponseEpisodesDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.Episode;
import com.example.rickandmortyapp.model.ExternalLink;
import com.example.rickandmortyapp.repository.EpisodeRepository;
import com.example.rickandmortyapp.repository.ExternalLinkRepository;
import com.example.rickandmortyapp.service.ExternalDataService;
import com.example.rickandmortyapp.service.HttpClient;
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
@Service("episodeService")
public class EpisodeServiceImpl implements ExternalDataService {
    private final HttpClient httpClient;
    private final EpisodeRepository episodeRepository;
    private final ExternalLinkRepository externalLinkRepository;
    private final ResponseMapper<EpisodeResponseDto, ApiEpisodeDto, Episode> episodeMapper;

    public EpisodeServiceImpl(
            HttpClient httpClient, EpisodeRepository episodeRepository,
            ExternalLinkRepository externalLinkRepository,
            ResponseMapper<EpisodeResponseDto, ApiEpisodeDto, Episode> episodeMapper) {
        this.httpClient = httpClient;
        this.episodeRepository = episodeRepository;
        this.externalLinkRepository = externalLinkRepository;
        this.episodeMapper = episodeMapper;
    }

    @Override
    public void syncExternalData() {
        log.info("EpisodeServiceImpl.syncExternalData started ...");
        ApiResponseEpisodesDto apiResponseEpisodesDto = null;
        do {
            String url = apiResponseEpisodesDto == null
                    ? "https://rickandmortyapi.com/api/episode"
                    : apiResponseEpisodesDto.getInfo().getNext();
            apiResponseEpisodesDto = httpClient.get(url, ApiResponseEpisodesDto.class);
            saveToDb(apiResponseEpisodesDto);
        } while (apiResponseEpisodesDto.getInfo().getNext() != null);
        log.info("EpisodeServiceImpl.syncExternalData done!");
    }

    private void saveToDb(ApiResponseEpisodesDto apiResponseEpisodesDto) {
        Map<Long, ApiEpisodeDto> externalDtos = Arrays.stream(apiResponseEpisodesDto.getResults())
                .collect(Collectors.toMap(ApiEpisodeDto::getId, Function.identity()));
        Set<Long> externalIds = externalDtos.keySet();

        List<Episode> existingEpisodes = episodeRepository.findAllByExternalIdIn(externalIds);
        Map<Long, Episode> existingEpisodesWithIds = existingEpisodes.stream()
                .collect(Collectors.toMap(Episode::getExternalId, Function.identity()));
        Set<Long> existingIds = existingEpisodesWithIds.keySet();

        List<Episode> episodesToUpdate = existingEpisodes.stream()
                .map(e -> updateFieldEpisode(e, externalDtos.get(e.getExternalId())))
                .collect(Collectors.toList());
        episodeRepository.saveAll(episodesToUpdate);

        externalIds.removeAll(existingIds);
        List<Episode> episodesToSave = externalIds.stream()
                .map(id -> episodeMapper.parseApiEntityResponseDto(externalDtos.get(id)))
                .collect(Collectors.toList());
        episodeRepository.saveAll(episodesToSave);
    }

    private Episode updateFieldEpisode(Episode oldEpisode, ApiEpisodeDto dto) {
        Episode newEpisode = episodeMapper.parseApiEntityResponseDto(dto);
        oldEpisode.setEpisode(newEpisode.getEpisode());
        oldEpisode.setAirDate(newEpisode.getAirDate());
        oldEpisode.setName(newEpisode.getName());

        Map<Long, ExternalLink> oldEpisodeCharacters = oldEpisode.getCharacters().stream()
                .collect(Collectors.toMap(ExternalLink::getExternalId, Function.identity()));
        Map<Long, ExternalLink> newEpisodeCharacters = newEpisode.getCharacters().stream()
                .collect(Collectors.toMap(ExternalLink::getExternalId, Function.identity()));

        Set<Long> idsToDelete = Set.copyOf(oldEpisodeCharacters.keySet().stream()
                .filter(k -> newEpisodeCharacters.get(k) == null)
                .collect(Collectors.toList()));
        if (idsToDelete.size() > 0) {
            externalLinkRepository.deleteAllByParentIdAndExternalIdIn(oldEpisode.getId(),
                    idsToDelete);
        }

        List<ExternalLink> externalLinksToUpdate = newEpisode.getCharacters().stream()
                .peek(c -> c.setId(oldEpisodeCharacters.get(c.getExternalId()).getId()))
                .collect(Collectors.toList());
        oldEpisode.setCharacters(externalLinksToUpdate);
        return oldEpisode;
    }
}
