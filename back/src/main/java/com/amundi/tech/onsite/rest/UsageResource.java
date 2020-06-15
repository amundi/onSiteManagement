package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.WorkingDayRepository;
import com.amundi.tech.onsite.model.usage.ParkingUsageImpl;
import com.amundi.tech.onsite.model.usage.RestaurantUsageImpl;
import com.amundi.tech.onsite.model.usage.SiteUsageImpl;
import com.amundi.tech.onsite.service.UsageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/usage")
@AllArgsConstructor
@Tag(name = "usage", description = "get sites & restaurants usages")
public class UsageResource {

    private final UsageService usageService;
    private final WorkingDayRepository workingDayRepository;

    @GetMapping(path = "/site/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SiteUsageImpl> usagesBySite(@PathVariable("id") long siteId,
                                            @RequestParam(value = "startDate", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam(value = "limit", required = false) Integer limit) {
        return (startDate!=null && limit !=null)?
                usageService.getSiteUsages(startDate, LocalDate.from(startDate).plusDays(limit), siteId):
                usageService.getSiteUsages(siteId);
    }

    @GetMapping(path = "/restaurant/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantUsageImpl> usagesByRestaurant(@PathVariable("id") long restaurantId,
                                                        @RequestParam(value = "startDate", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                        @RequestParam(value = "limit", required = false) Integer limit) {
        return (startDate!=null && limit !=null)?
                usageService.getRestaurantUsages(startDate, LocalDate.from(startDate).plusDays(limit), restaurantId):
                usageService.getRestaurantUsages(restaurantId);
    }

    @GetMapping(path = "/parking/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParkingUsageImpl> usagesByParking(@PathVariable("id") long parkingId,
                                            @RequestParam(value = "startDate", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam(value = "limit", required = false) Integer limit) {
        return (startDate!=null && limit !=null)?
                usageService.getParkingUsages(startDate, LocalDate.from(startDate).plusDays(limit), parkingId):
                usageService.getParkingUsages(parkingId);
    }
}
