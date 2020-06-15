package com.amundi.tech.onsite.service;

import com.amundi.tech.onsite.db.*;
import com.amundi.tech.onsite.db.model.*;
import com.amundi.tech.onsite.model.Parking;
import com.amundi.tech.onsite.model.mapper.UsageMapper;
import com.amundi.tech.onsite.model.usage.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UsageService {

    private static final int DEFAULT_NB_DAYS = 30;

    private final SiteDefinitionRepository siteDefinitionRepository;
    private final SiteCapacityRepository siteCapacityRepository;

    private final RestaurantDefinitionRepository restaurantDefinitionRepository;
    private final RestaurantCapacityRepository restaurantCapacityRepository;

    private final ParkingDefinitionRepository parkingDefinitionRepository;
    private final ParkingCapacityRepository parkingCapacityRepository;

    private final WorkingDayRepository workingDayRepository;

    private final UsageMapper siteUsageMapper;

    public List<SiteUsageImpl> getSiteUsages(Long id) {
        return getSiteUsages(LocalDate.now(), LocalDate.now().plusDays(DEFAULT_NB_DAYS), id);
    }

    public List<SiteUsageImpl> getSiteUsages(LocalDate startDate, LocalDate endDate, Long id) {
        List<SiteUsageReport> reports = getSiteReports(startDate, endDate, id);
        return reports.stream().map(siteUsageMapper::getSimpleUsage).collect(Collectors.toList());
    }

    public List<SiteUsageReport> getSiteReports(Long siteId) {
        return getSiteReports(LocalDate.now(), LocalDate.now().plusDays(DEFAULT_NB_DAYS), siteId);
    }

    public List<RestaurantUsageImpl> getRestaurantUsages(Long id) {
        return getRestaurantUsages(LocalDate.now(), LocalDate.now().plusDays(DEFAULT_NB_DAYS), id);
    }

    public List<RestaurantUsageImpl> getRestaurantUsages(LocalDate startDate, LocalDate endDate, Long id) {
        List<RestaurantUsageReport> reports = getRestaurantReports(startDate, endDate, id);
        return reports.stream().map(siteUsageMapper::getSimpleUsage).collect(Collectors.toList());
    }

    public List<RestaurantUsageReport> getRestaurantReports(Long id) {
        return getRestaurantReports(LocalDate.now(), LocalDate.now().plusDays(DEFAULT_NB_DAYS), id);
    }

    public List<ParkingUsageImpl> getParkingUsages(Long id) {
        return getParkingUsages(LocalDate.now(), LocalDate.now().plusDays(DEFAULT_NB_DAYS), id);
    }

    public List<ParkingUsageImpl> getParkingUsages(LocalDate startDate, LocalDate endDate, Long id) {
        List<ParkingUsageReport> reports = getParkingReports(startDate, endDate, id);
        return reports.stream().map(siteUsageMapper::getSimpleUsage).collect(Collectors.toList());
    }

    public List<ParkingUsageReport> getParkingReports(Long id) {
        return getParkingReports(LocalDate.now(), LocalDate.now().plusDays(DEFAULT_NB_DAYS), id);
    }

    public List<SiteUsageReport> getSiteReports(LocalDate startDate, LocalDate endDate, Long siteId) {

        List<SiteUsageReport> reports = new ArrayList<>();

        Optional<SiteDefinition> optionalSiteDefinition = siteDefinitionRepository.findById(siteId);
        if (optionalSiteDefinition.isPresent()) {
            SiteDefinition site = optionalSiteDefinition.get();

            // Reading site capacities -------------------------------------
            List<SiteCapacity> capacities = siteCapacityRepository
                    .findAllBySite(SiteDefinition.builder().id(siteId).build());
            for (SiteCapacity capacity : capacities) {
                for (LocalDate d = capacity.getStartDate(); !d.isAfter(capacity.getEndDate()); d = d.plusDays(1)) {
                    if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                        reports.add(SiteUsageReport.builder()
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
                                .offSite_users(new HashSet<>())

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

                                .build());
                    }
                }
            }
            reports.sort(Comparator.comparing(SiteUsageReport::getDate));

            Map<LocalDate, SiteUsageReport> reportsByDate = new HashMap<>();
            reports.forEach(u -> reportsByDate.put(u.getDate(), u));

            // Reading usages --------------------------------------------
            List<WorkingDay> workingDays = workingDayRepository.findAllBySiteAndDateAfter(site, startDate.minusDays(1));
            for (WorkingDay day : workingDays) {
                if (reportsByDate.containsKey(day.getDate())) {
                    SiteUsageReport usage = reportsByDate.get(day.getDate());

                    if (day.getArrival() == 700) usage.getH700_users().add(day.getUser());
                    else if (day.getArrival() == 730) usage.getH730_users().add(day.getUser());
                    else if (day.getArrival() == 800) usage.getH800_users().add(day.getUser());
                    else if (day.getArrival() == 830) usage.getH830_users().add(day.getUser());
                    else if (day.getArrival() == 900) usage.getH900_users().add(day.getUser());
                    else if (day.getArrival() == 930) usage.getH930_users().add(day.getUser());
                    else if (day.getArrival() == 1000) usage.getH1000_users().add(day.getUser());
                    else if (day.getArrival() == 1030) usage.getH1030_users().add(day.getUser());

                    if (day.isOnSite()) usage.getOnSite_users().add(day.getUser());
                    if (day.isOffSite()) usage.getOffSite_users().add(day.getUser());

                    if (day.getDeparture() == 1700) usage.getH1700_users().add(day.getUser());
                    else if (day.getDeparture() == 1730) usage.getH1730_users().add(day.getUser());
                    else if (day.getDeparture() == 1800) usage.getH1800_users().add(day.getUser());
                    else if (day.getDeparture() == 1830) usage.getH1830_users().add(day.getUser());
                    else if (day.getDeparture() == 1900) usage.getH1900_users().add(day.getUser());
                }
            }
        }
        return reports;
    }

    public List<RestaurantUsageReport> getRestaurantReports(LocalDate startDate, LocalDate endDate, Long restaurantId) {

        List<RestaurantUsageReport> reports = new ArrayList<>();

        Optional<RestaurantDefinition> optionalRestaurantDefinition = restaurantDefinitionRepository.findById(restaurantId);
        if (optionalRestaurantDefinition.isPresent()) {
            RestaurantDefinition restaurant = optionalRestaurantDefinition.get();

            // Reading restaurant capacities -------------------------------------
            List<RestaurantCapacity> capacities = restaurantCapacityRepository
                    .findAllByRestaurant(RestaurantDefinition.builder().id(restaurantId).build());
            for (RestaurantCapacity capacity : capacities) {
                for (LocalDate d = capacity.getStartDate(); !d.isAfter(capacity.getEndDate()); d = d.plusDays(1)) {
                    if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                        reports.add(RestaurantUsageReport.builder()
                                .date(d)
                                .h1130_users(new HashSet<>())
                                .h1200_users(new HashSet<>())
                                .h1230_users(new HashSet<>())
                                .h1300_users(new HashSet<>())
                                .h1330_users(new HashSet<>())
                                .h1130(capacity.getArrival_1130())
                                .h1200(capacity.getArrival_1200())
                                .h1230(capacity.getArrival_1230())
                                .h1300(capacity.getArrival_1300())
                                .h1330(capacity.getArrival_1330())
                                .build());
                    }
                }
            }
            reports.sort(Comparator.comparing(RestaurantUsageReport::getDate));

            Map<LocalDate, RestaurantUsageReport> reportsByDate = new HashMap<>();
            reports.forEach(u -> reportsByDate.put(u.getDate(), u));

            // Reading usages --------------------------------------------
            List<WorkingDay> workingDays = workingDayRepository.findAllByRestaurantAndDateAfter(restaurant, startDate.minusDays(1));
            for (WorkingDay day : workingDays) {
                if (reportsByDate.containsKey(day.getDate())) {
                    RestaurantUsageReport usage = reportsByDate.get(day.getDate());

                    if (day.getLunch() == 1130) usage.getH1130_users().add(day.getUser());
                    else if (day.getLunch() == 1200) usage.getH1200_users().add(day.getUser());
                    else if (day.getLunch() == 1230) usage.getH1230_users().add(day.getUser());
                    else if (day.getLunch() == 1300) usage.getH1300_users().add(day.getUser());
                    else if (day.getLunch() == 1330) usage.getH1300_users().add(day.getUser());
                }
            }
        }
        return reports;
    }

    public List<ParkingUsageReport> getParkingReports(LocalDate startDate, LocalDate endDate, Long parkingId) {

        List<ParkingUsageReport> reports = new ArrayList<>();

        Optional<ParkingDefinition> optionalParkingDefinition = parkingDefinitionRepository.findById(parkingId);
        if (optionalParkingDefinition.isPresent()) {
            ParkingDefinition parking = optionalParkingDefinition.get();

            // Reading parking capacities -------------------------------------
            List<ParkingCapacity> capacities = parkingCapacityRepository
                    .findAllByParking(ParkingDefinition.builder().id(parkingId).build());
            for (ParkingCapacity capacity : capacities) {
                for (LocalDate d = capacity.getStartDate(); !d.isAfter(capacity.getEndDate()); d = d.plusDays(1)) {
                    if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                        reports.add(ParkingUsageReport.builder()
                                .date(d)
                                .car_users(new HashSet<>())
                                .moto_users(new HashSet<>())
                                .bike_users(new HashSet<>())
                                .car(capacity.getCar())
                                .moto(capacity.getMoto())
                                .bike(capacity.getBike())
                                .build());
                    }
                }
            }
            reports.sort(Comparator.comparing(ParkingUsageReport::getDate));

            Map<LocalDate, ParkingUsageReport> reportsByDate = new HashMap<>();
            reports.forEach(u -> reportsByDate.put(u.getDate(), u));

            // Reading usages --------------------------------------------
            List<WorkingDay> workingDays = workingDayRepository.findAllByParkingAndDateAfter(parking, startDate.minusDays(1));
            for (WorkingDay day : workingDays) {
                if (reportsByDate.containsKey(day.getDate())) {
                    ParkingUsageReport usage = reportsByDate.get(day.getDate());

                    if (Parking.CAR.equals(day.getParkingType())) usage.getCar_users().add(day.getUser());
                    else if (Parking.MOTO.equals(day.getParkingType())) usage.getMoto_users().add(day.getUser());
                    else if (Parking.BIKE.equals(day.getParkingType())) usage.getBike_users().add(day.getUser());
                }
            }
        }
        return reports;
    }
}
