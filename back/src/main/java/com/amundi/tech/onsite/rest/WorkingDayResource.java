package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.WorkingDayRepository;
import com.amundi.tech.onsite.db.model.SiteDefinition;
import com.amundi.tech.onsite.db.model.WorkingDay;
import com.amundi.tech.onsite.model.WorkingDays;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.amundi.tech.onsite.exception.AppException.*;

@RestController
@RequestMapping("/api/workingDays")
@AllArgsConstructor
@Tag(name = "workingDay", description = "user defined working days")
public class WorkingDayResource {

    private final WorkingDayRepository workingDayRepository;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WorkingDay> all() {
        return workingDayRepository.findAll();
    }

    @GetMapping(path = "/bySite", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WorkingDay> bySite(@RequestParam("id") Long siteId) {
        return workingDayRepository.findAllBySite(SiteDefinition.builder().id(siteId).build());
    }

    @GetMapping(path = "/byUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WorkingDay> byUser(@RequestParam("id") String user) {
        return workingDayRepository.findAllByUser(user);
    }

    @PostMapping(path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkingDays update(@RequestBody WorkingDays workingDays) {
        if (workingDays == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_PARAM);
        }
        if (workingDays.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_USER);
        }
        if (workingDays.getSite() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_SITE);
        }

        workingDayRepository.deleteAllBySiteIsNotAndUser(
                SiteDefinition.builder().id(workingDays.getSite()).build(),
                workingDays.getUser());

        for (WorkingDay workingDay : workingDays.getWorkingDays()) {
            workingDayRepository.deleteAllBySiteAndUserAndAndDateEquals(
                    workingDay.getSite(), workingDay.getUser(), workingDay.getDate()
            );
            workingDayRepository.save(workingDay);
        }

        List<WorkingDay> wds = workingDays.getStartDate() != null ?
                workingDayRepository.findAllByUserAndDateAfter(workingDays.getUser(), workingDays.getStartDate()) :
                workingDayRepository.findAllByUser(workingDays.getUser());

        return WorkingDays.builder()
                .user(workingDays.getUser())
                .workingDays(wds)
                .build();
    }

    @PostMapping(path = "/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkingDays delete(@RequestBody WorkingDays workingDays) {
        if (workingDays == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_PARAM);
        }
        if (workingDays.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_USER);
        }
        if (workingDays.getSite() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_SITE);
        }

        workingDayRepository.deleteAllBySiteIsNotAndUser(
                SiteDefinition.builder().id(workingDays.getSite()).build(),
                workingDays.getUser());

        for (WorkingDay workingDay : workingDays.getWorkingDays()) {
            workingDayRepository.deleteAllBySiteAndUserAndAndDateEquals(
                    workingDay.getSite(), workingDay.getUser(), workingDay.getDate()
            );
        }

        List<WorkingDay> wds = workingDays.getStartDate() != null ?
                workingDayRepository.findAllByUserAndDateAfter(workingDays.getUser(), workingDays.getStartDate()) :
                workingDayRepository.findAllByUser(workingDays.getUser());

        return WorkingDays.builder()
                .user(workingDays.getUser())
                .workingDays(wds)
                .build();
    }

    @PostMapping(path = "/byUser",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkingDays updateByUser(@RequestBody WorkingDays workingDays) {
        if (workingDays == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_PARAM);
        }
        if (workingDays.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_USER);
        }
        workingDayRepository.deleteAllByUser(workingDays.getUser());
        return WorkingDays.builder()
                .user(workingDays.getUser())
                .workingDays(workingDayRepository.saveAll(workingDays.getWorkingDays())).build();
    }


}
