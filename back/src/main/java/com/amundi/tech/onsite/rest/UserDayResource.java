package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.WorkingDayRepository;
import com.amundi.tech.onsite.db.model.ParkingDefinition;
import com.amundi.tech.onsite.db.model.RestaurantDefinition;
import com.amundi.tech.onsite.db.model.SiteDefinition;
import com.amundi.tech.onsite.db.model.WorkingDay;
import com.amundi.tech.onsite.model.UserDay;
import com.amundi.tech.onsite.model.UserDays;
import com.amundi.tech.onsite.model.mapper.WorkingDayMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.amundi.tech.onsite.exception.AppException.*;

@RestController
@RequestMapping("/api/day")
@AllArgsConstructor
@Tag(name = "day", description = "user defined working days")
public class UserDayResource {

    private final WorkingDayMapper workingDayMapper;
    private final WorkingDayRepository workingDayRepository;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDay> all() {
        return workingDayRepository.findAll()
                .stream()
                .map(workingDayMapper::convert)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/site/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDay> site(@PathVariable("id") Long siteId,
                              @RequestParam(value = "startDate", required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        LocalDate date = startDate != null ? startDate.minusDays(1) : LocalDate.now().minusDays(1);
        return workingDayRepository.findAllBySiteAndDateAfter(SiteDefinition.builder().id(siteId).build(), date)
                .stream()
                .map(workingDayMapper::convert)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/restaurant/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDay> restaurant(@PathVariable("id") Long restaurantId,
                                       @RequestParam(value = "startDate", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        LocalDate date = startDate != null ? startDate.minusDays(1) : LocalDate.now().minusDays(1);
        return workingDayRepository.findAllByRestaurantAndDateAfter(RestaurantDefinition.builder().id(restaurantId).build(), date)
                .stream()
                .map(workingDayMapper::convert)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/parking/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDay> parking(@PathVariable("id") Long parkingId,
                                    @RequestParam(value = "startDate", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        LocalDate date = startDate != null ? startDate.minusDays(1) : LocalDate.now().minusDays(1);
        return workingDayRepository.findAllByParkingAndDateAfter(ParkingDefinition.builder().id(parkingId).build(), date)
                .stream()
                .map(workingDayMapper::convert)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDay> user(@PathVariable("id") String user,
                                 @RequestParam(value = "startDate", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        LocalDate date = startDate != null ? startDate.minusDays(1) : LocalDate.now().minusDays(1);
        List<UserDay> userDays = workingDayRepository.findAllByUserAndDateAfter(user, date)
                .stream()
                .map(workingDayMapper::convert)
                .collect(Collectors.toList());
        return userDays;
    }

    @PostMapping(path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDays update(@RequestBody UserDays userDays) {
        if (userDays == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_PARAM);
        }

        if (userDays.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_USER);
        }

        for (UserDay userDay : userDays.getUserDays()) {
            workingDayRepository.deleteAllByUserAndAndDateEquals(userDay.getUser(), userDay.getDate());
            workingDayRepository.save(workingDayMapper.convert(userDay));
        }

        List<WorkingDay> wds = userDays.getStartDate() != null ?
                workingDayRepository.findAllByUserAndDateAfter(userDays.getUser(), userDays.getStartDate()) :
                workingDayRepository.findAllByUser(userDays.getUser());
        List<UserDay> ud = wds.stream().map(workingDayMapper::convert).collect(Collectors.toList());

        return UserDays.builder()
                .user(userDays.getUser())
                .userDays(ud)
                .build();
    }

    @PostMapping(path = "/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDays delete(@RequestBody UserDays userDays) {
        if (userDays == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_PARAM);
        }
        if (userDays.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_USER);
        }

        for (UserDay userDay : userDays.getUserDays()) {
            workingDayRepository.deleteAllByUserAndAndDateEquals(userDay.getUser(), userDay.getDate());
        }

        List<WorkingDay> wds = userDays.getStartDate() != null ?
                workingDayRepository.findAllByUserAndDateAfter(userDays.getUser(), userDays.getStartDate()) :
                workingDayRepository.findAllByUser(userDays.getUser());
        List<UserDay> ud = wds.stream().map(workingDayMapper::convert).collect(Collectors.toList());

        return UserDays.builder()
                .user(userDays.getUser())
                .userDays(ud)
                .build();
    }
}
