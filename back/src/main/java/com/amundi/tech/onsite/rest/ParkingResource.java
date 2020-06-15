package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.ParkingCapacityRepository;
import com.amundi.tech.onsite.db.ParkingDefinitionRepository;
import com.amundi.tech.onsite.db.model.ParkingCapacity;
import com.amundi.tech.onsite.db.model.ParkingDefinition;
import com.amundi.tech.onsite.exception.AppException;
import com.amundi.tech.onsite.model.Parking;
import com.amundi.tech.onsite.model.usage.ParkingUsageImpl;
import com.amundi.tech.onsite.service.UsageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/parking")
@AllArgsConstructor
@Tag(name = "parking", description = "parking administration")
public class ParkingResource {

    private final ParkingDefinitionRepository parkingDefinitionRepository;
    private final ParkingCapacityRepository parkingCapacityRepository;
    private final UsageService usageService;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Parking> all(@RequestParam(value = "addUsage", required = false) boolean addUsage) {
        List<Parking> parkings = new ArrayList<>();
        List<ParkingDefinition> definitions = parkingDefinitionRepository.findAll();
        definitions.parallelStream().forEach(d -> {
            List<ParkingCapacity> capacities = parkingCapacityRepository.findAllByParking(d);
            capacities.forEach(c -> c.setParking(null));
            Parking parking = Parking.builder()
                    .definition(d)
                    .capacities(capacities)
                    .build();
            if (addUsage) {
                List<ParkingUsageImpl> usages = usageService.getParkingUsages(d.getId());
                parking.setUsages(usages);
            }
            parkings.add(parking);
        });
        return parkings;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Parking byId(@PathVariable("id") long parkingId) {
        Optional<ParkingDefinition> definitionOptional = parkingDefinitionRepository.findById(parkingId);
        if (!definitionOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(AppException.PARKING_DOESNT_EXIST, parkingId));
        }
        ParkingDefinition definition = definitionOptional.get();
        Parking parking = Parking.builder()
                .definition(definition)
                .capacities(parkingCapacityRepository.findAllByParking(definition))
                .build();

        List<ParkingUsageImpl> usages = usageService.getParkingUsages(definition.getId());
        parking.setUsages(usages);
        return parking;
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Parking save(@RequestBody Parking parking) {
        if (parking == null || parking.getDefinition() == null || parking.getDefinition().getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, AppException.EMPTY_SITE);
        }
        ParkingDefinition d = parking.getDefinition();

        try {
            sanitizeCapacities(parking);
        } catch (AppException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        d = parkingDefinitionRepository.save(d);
        parkingCapacityRepository.deleteAllByParking(d);

        List<ParkingCapacity> updatedCapacities = new ArrayList<>();
        if (parking.getCapacities() != null) {
            for (ParkingCapacity capacity : parking.getCapacities()) {
                capacity.setParking(d);
                updatedCapacities.add(parkingCapacityRepository.save(capacity));
            }
        }
        return Parking.builder().definition(d).capacities(updatedCapacities).build();
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") long siteId) {
        parkingCapacityRepository.deleteAllByParking(ParkingDefinition.builder().id(siteId).build());
        parkingDefinitionRepository.deleteById(siteId);
    }

    private void sanitizeCapacities(Parking parking) throws AppException {
        List<ParkingCapacity> capacities = parking.getCapacities();

        if (capacities != null) {
            List<ParkingCapacity> updated = new ArrayList<>();
            capacities.sort(Comparator.comparing(ParkingCapacity::getStartDate));
            LocalDate date = null;
            for (ParkingCapacity capacity : capacities) {
                if (capacity.getStartDate() == null || capacity.getEndDate() == null) {
                    throw new AppException("bad capacity definition");
                }
                if (date != null && !capacity.getStartDate().isAfter(date)) {
                    capacity.setStartDate(date);
                }
                if (!capacity.getStartDate().isAfter(capacity.getEndDate())) {
                    updated.add(capacity);
                    date = capacity.getEndDate().plusDays(1);
                }
            }
            parking.setCapacities(updated);
        }
    }
}
