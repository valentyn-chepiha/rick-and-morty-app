package com.example.rickandmortyapp.controller;

import com.example.rickandmortyapp.service.ScheduledService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class UpdateDataController {
    private final ScheduledService scheduledService;

    public UpdateDataController(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }

    @GetMapping
    public String runUpdate() {
        scheduledService.syncExternalData();
        return "Done!!";
    }
}
