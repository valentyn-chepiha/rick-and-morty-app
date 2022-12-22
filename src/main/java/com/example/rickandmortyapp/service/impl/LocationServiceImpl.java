package com.example.rickandmortyapp.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.example.rickandmortyapp.dto.LocationResponseDto;
import com.example.rickandmortyapp.dto.external.ApiLocationDto;
import com.example.rickandmortyapp.dto.external.ApiResponseLocationsDto;
import com.example.rickandmortyapp.dto.mapper.ResponseMapper;
import com.example.rickandmortyapp.model.ExternalLink;
import com.example.rickandmortyapp.model.Location;
import com.example.rickandmortyapp.repository.ExternalLinkRepository;
import com.example.rickandmortyapp.repository.LocationRepository;
import com.example.rickandmortyapp.service.ExternalDataService;
import com.example.rickandmortyapp.service.HttpClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@Service("locationService")
public class LocationServiceImpl implements ExternalDataService {
    private final HttpClient httpClient;
    private final LocationRepository locationRepository;
    private final ExternalLinkRepository externalLinkRepository;
    private final ResponseMapper<LocationResponseDto, ApiLocationDto, Location> locationMapper;

    public LocationServiceImpl(HttpClient httpClient, LocationRepository locationRepository,
                               ExternalLinkRepository externalLinkRepository,
                               ResponseMapper<LocationResponseDto, ApiLocationDto, Location> locationMapper) {
        this.httpClient = httpClient;
        this.locationRepository = locationRepository;
        this.externalLinkRepository = externalLinkRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public void syncExternalData() {
        log.info("LocationServiceImpl.syncExternalData started ...");
        ApiResponseLocationsDto apiResponseLocationsDto = null;
        do {
            String url = apiResponseLocationsDto == null
                    ? "https://rickandmortyapi.com/api/location"
                    : apiResponseLocationsDto.getInfo().getNext();
            apiResponseLocationsDto = httpClient.get(url, ApiResponseLocationsDto.class);
            saveToDb(apiResponseLocationsDto);
        } while (apiResponseLocationsDto.getInfo().getNext() != null);
        log.info("LocationServiceImpl.syncExternalData done!");
    }

    private void saveToDb(ApiResponseLocationsDto dtos) {
        Map<Long, ApiLocationDto> externalDtos = Arrays.stream(dtos.getResults())
                .collect(Collectors.toMap(ApiLocationDto::getId, Function.identity()));
        Set<Long> externalIds = externalDtos.keySet();

        List<Location> existingLocations = locationRepository.findAllByExternalIdIn(externalIds);
        Map<Long, Location> existingLocationsWithIds = existingLocations.stream()
                .collect(Collectors.toMap(Location::getExternalId, Function.identity()));
        Set<Long> existingIds = existingLocationsWithIds.keySet();

        List<Location> locationToUpdate = existingLocations.stream()
                .map(l -> updateFieldLocation(l, externalDtos.get(l.getExternalId())))
                .collect(Collectors.toList());
        locationRepository.saveAll(locationToUpdate);

        externalIds.removeAll(existingIds);
        List<Location> locationToSave = externalIds.stream()
                .map(id -> locationMapper.parseApiEntityResponseDto(externalDtos.get(id)))
                .collect(Collectors.toList());
        locationRepository.saveAll(locationToSave);
    }

    private Location updateFieldLocation(Location oldLocation, ApiLocationDto dto) {
        Location newLocation = locationMapper.parseApiEntityResponseDto(dto);
        oldLocation.setType(newLocation.getType());
        oldLocation.setDimension(newLocation.getDimension());
        oldLocation.setName(newLocation.getName());

        Map<Long, ExternalLink> oldLocationResidents = oldLocation.getResidents().stream()
                .collect(Collectors.toMap(ExternalLink::getExternalId, Function.identity()));
        Map<Long, ExternalLink> newLocationResidents = newLocation.getResidents().stream()
                .collect(Collectors.toMap(ExternalLink::getExternalId, Function.identity()));

        Set<Long> idsToDelete = Set.copyOf(oldLocationResidents.keySet().stream()
                .filter(k -> newLocationResidents.get(k) == null)
                .collect(Collectors.toList()));
        if (idsToDelete.size() > 0) {
            externalLinkRepository.deleteAllByParentIdAndExternalIdIn(oldLocation.getId(), idsToDelete);
        }

        List<ExternalLink> externalLinkToUpdate = newLocation.getResidents().stream()
                .peek(l -> l.setId(oldLocationResidents.get(l.getExternalId()).getId()))
                .collect(Collectors.toList());
        oldLocation.setResidents(externalLinkToUpdate);

        return oldLocation;
    }
}
