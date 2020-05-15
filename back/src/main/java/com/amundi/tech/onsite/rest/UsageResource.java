package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.RestaurantCapacityRepository;
import com.amundi.tech.onsite.db.SiteCapacityRepository;
import com.amundi.tech.onsite.db.SiteDefinitionRepository;
import com.amundi.tech.onsite.db.WorkingDayRepository;
import com.amundi.tech.onsite.db.model.RestaurantCapacity;
import com.amundi.tech.onsite.db.model.SiteCapacity;
import com.amundi.tech.onsite.db.model.SiteDefinition;
import com.amundi.tech.onsite.db.model.WorkingDay;
import com.amundi.tech.onsite.model.SimpleSiteUsage;
import com.amundi.tech.onsite.model.SiteReport;
import com.amundi.tech.onsite.model.SiteUsage;
import com.amundi.tech.onsite.util.ModelUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/usage")
@AllArgsConstructor
@Tag(name = "usage", description = "get sites & restaurants usages")
public class UsageResource {

    private static final int DEFAULT_LIMIT = 60;

    private final SiteDefinitionRepository siteDefinitionRepository;
    private final SiteCapacityRepository siteCapacityRepository;
    private final RestaurantCapacityRepository restaurantCapacityRepository;
    private final WorkingDayRepository workingDayRepository;

    @GetMapping(path = "/site/basic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SimpleSiteUsage> basicSite(@PathVariable("id") long siteId,
                                           @RequestParam(value = "startDate", required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam(value = "limit", required = false) Integer limit) {
        List<SiteReport> reports = getReports(siteId, startDate, limit);
        return reports.stream().map(ModelUtils::getFromSiteReport).collect(Collectors.toList());
    }

    @GetMapping(path = "/site/report/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SiteReport> reportSite(@PathVariable("id") long siteId,
                                       @RequestParam(value = "startDate", required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam(value = "limit", required = false) Integer limit) {
        return getReports(siteId, startDate, limit);
    }

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WorkingDay> user(@PathVariable("id") String user,
                                 @RequestParam(value = "startDate", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate
    ) {
        LocalDate date = startDate != null ? startDate.minusDays(1) : LocalDate.now().minusDays(1);
        return workingDayRepository.findAllByUserAndDateAfter(user, date);
    }


    private List<SiteReport> getReports(long siteId, LocalDate startDate, Integer limit) {

        // if start date == null, on prend aujourd'hui
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        LocalDate endDate = startDate.plusDays(limit != null ? limit : DEFAULT_LIMIT);
        List<SiteReport> reports = new ArrayList<>();

        Optional<SiteDefinition> optionalSiteDefinition = siteDefinitionRepository.findById(siteId);
        if (optionalSiteDefinition.isPresent()) {
            SiteDefinition site = optionalSiteDefinition.get();

            // Reading site capacities -------------------------------------
            List<SiteCapacity> capacities = siteCapacityRepository
                    .findAllBySite(SiteDefinition.builder().id(siteId).build());
            for (SiteCapacity capacity : capacities) {
                for (LocalDate d = capacity.getStartDate(); !d.isAfter(capacity.getEndDate()); d = d.plusDays(1)) {
                    if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                        reports.add(SiteReport.builder()
                                .date(d)
                                .h700(capacity.getArrival_700())
                                .h730(capacity.getArrival_730())
                                .h800(capacity.getArrival_800())
                                .h830(capacity.getArrival_830())
                                .h900(capacity.getArrival_900())
                                .h930(capacity.getArrival_930())
                                .h1000(capacity.getArrival_1000())
                                .h1030(capacity.getArrival_1030())
                                .h700_users(new HashSet<>())
                                .h730_users(new HashSet<>())
                                .h800_users(new HashSet<>())
                                .h830_users(new HashSet<>())
                                .h900_users(new HashSet<>())
                                .h930_users(new HashSet<>())
                                .h1000_users(new HashSet<>())
                                .h1030_users(new HashSet<>())

                                .onSite(capacity.getOnSite())
                                .onSite_users(new HashSet<>())

                                .h1130_users(new HashSet<>())
                                .h1200_users(new HashSet<>())
                                .h1230_users(new HashSet<>())
                                .h1300_users(new HashSet<>())
                                .h1330_users(new HashSet<>())

                                .h1700(capacity.getDeparture_1700())
                                .h1730(capacity.getDeparture_1730())
                                .h1800(capacity.getDeparture_1800())
                                .h1830(capacity.getDeparture_1830())
                                .h1900(capacity.getDeparture_1900())
                                .h1700_users(new HashSet<>())
                                .h1730_users(new HashSet<>())
                                .h1800_users(new HashSet<>())
                                .h1830_users(new HashSet<>())
                                .h1900_users(new HashSet<>())

                                .car(capacity.getCar())
                                .moto(capacity.getMotorcycle())
                                .car_users(new HashSet<>())
                                .moto_users(new HashSet<>())
                                .build());
                    }
                }
            }
            reports.sort(Comparator.comparing(SiteReport::getDate));

            Map<LocalDate, SiteReport> reportsByDate = new HashMap<>();
            reports.forEach(u -> reportsByDate.put(u.getDate(), u));

            // Reading restaurant capacities -----------------------------
            if (site.getRestaurant() != null) {
                List<RestaurantCapacity> restaurantCapacities = restaurantCapacityRepository.findAllByRestaurant(site.getRestaurant());
                for (RestaurantCapacity capacity : restaurantCapacities) {
                    for (LocalDate d = capacity.getStartDate(); !d.isAfter(capacity.getEndDate()); d = d.plusDays(1)) {
                        // Here, we suppose that if no site capacity is defined, no restaurant capacity used
                        if (reportsByDate.containsKey(d)) {
                            SiteUsage usage = reportsByDate.get(d);
                            usage.setH1130(capacity.getArrival_1130());
                            usage.setH1200(capacity.getArrival_1200());
                            usage.setH1230(capacity.getArrival_1230());
                            usage.setH1300(capacity.getArrival_1300());
                            usage.setH1330(capacity.getArrival_1330());
                        }
                    }
                }
            }

            // Reading usages --------------------------------------------
            List<WorkingDay> workingDays = workingDayRepository.findAllBySiteAndDateAfter(site, startDate.minusDays(1));
            for (WorkingDay day : workingDays) {
                if (reportsByDate.containsKey(day.getDate())) {
                    SiteReport usage = reportsByDate.get(day.getDate());

                    if (day.getArrival() == 700) usage.getH700_users().add(day.getUser());
                    else if (day.getArrival() == 730) usage.getH730_users().add(day.getUser());
                    else if (day.getArrival() == 800) usage.getH800_users().add(day.getUser());
                    else if (day.getArrival() == 830) usage.getH830_users().add(day.getUser());
                    else if (day.getArrival() == 900) usage.getH900_users().add(day.getUser());
                    else if (day.getArrival() == 930) usage.getH930_users().add(day.getUser());
                    else if (day.getArrival() == 1000) usage.getH1000_users().add(day.getUser());
                    else if (day.getArrival() == 1030) usage.getH1030_users().add(day.getUser());

                    if(day.isOnSite()) usage.getOnSite_users().add(day.getUser());

                    if (day.getRestaurant() == 1130) usage.getH1130_users().add(day.getUser());
                    else if (day.getRestaurant() == 1200) usage.getH1200_users().add(day.getUser());
                    else if (day.getRestaurant() == 1230) usage.getH1230_users().add(day.getUser());
                    else if (day.getRestaurant() == 1300) usage.getH1300_users().add(day.getUser());
                    else if (day.getRestaurant() == 1330) usage.getH1330_users().add(day.getUser());

                    if (day.getDeparture() == 1700) usage.getH1700_users().add(day.getUser());
                    else if (day.getDeparture() == 1730) usage.getH1730_users().add(day.getUser());
                    else if (day.getDeparture() == 1800) usage.getH1800_users().add(day.getUser());
                    else if (day.getDeparture() == 1830) usage.getH1830_users().add(day.getUser());
                    else if (day.getDeparture() == 1900) usage.getH1900_users().add(day.getUser());

                    if (day.isMoto()) usage.getMoto_users().add(day.getUser());
                    if (day.isCar()) usage.getCar_users().add(day.getUser());
                }
            }
        }
        return reports;
    }
}
