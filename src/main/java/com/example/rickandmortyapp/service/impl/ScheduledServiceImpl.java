package com.example.rickandmortyapp.service.impl;

import com.example.rickandmortyapp.service.ExternalDataService;
import com.example.rickandmortyapp.service.PersonageService;
import com.example.rickandmortyapp.service.ScheduledService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ScheduledServiceImpl implements ScheduledService {
    private final PersonageService personageService;
    private final ExternalDataService episodeService;
    private final ExternalDataService locationService;

    public ScheduledServiceImpl(PersonageService personageService,
                                ExternalDataService episodeService,
                                ExternalDataService locationService) {
        this.personageService = personageService;
        this.episodeService = episodeService;
        this.locationService = locationService;
    }

    @Scheduled(cron = "0 8 * * * ?")
    @Override
    public void syncExternalData() {
        log.info("ScheduledServiceImpl start...");
        locationService.syncExternalData();
        episodeService.syncExternalData();
        personageService.syncExternalPersonages();
        log.info("ScheduledServiceImpl finish...");
    }
}
