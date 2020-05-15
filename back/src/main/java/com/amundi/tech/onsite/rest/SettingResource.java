package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.SettingRepository;
import com.amundi.tech.onsite.db.model.Setting;
import com.amundi.tech.onsite.model.Settings;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/setting")
@AllArgsConstructor
@Tag(name = "setting", description = "settings administration")
public class SettingResource {

    private final SettingRepository settingsRepository;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Settings all() {
        return Settings.builder().settings(settingsRepository.findAll()).build();
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Setting save(@RequestBody Setting setting) {
        return settingsRepository.save(setting);
    }

    @PostMapping(path = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Settings saveAll(@RequestBody Settings settings) {
        settingsRepository.deleteAll();
        return Settings.builder().settings(settingsRepository.saveAll(settings.getSettings())).build();
    }
}