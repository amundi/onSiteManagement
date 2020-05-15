package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.RestaurantCapacityRepository;
import com.amundi.tech.onsite.db.RestaurantDefinitionRepository;
import com.amundi.tech.onsite.db.model.RestaurantCapacity;
import com.amundi.tech.onsite.db.model.RestaurantDefinition;
import com.amundi.tech.onsite.exception.AppException;
import com.amundi.tech.onsite.model.Restaurant;
import com.amundi.tech.onsite.model.RestaurantUsage;
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

import static com.amundi.tech.onsite.exception.AppException.EMPTY_SITE;
import static com.amundi.tech.onsite.exception.AppException.RESTAURANT_DOESNT_EXIST;

@Slf4j
@RestController
@RequestMapping("/api/restaurant")
@AllArgsConstructor
@Tag(name = "restaurant", description = "restaurant administration")
public class RestaurantResource {

    private final RestaurantDefinitionRepository restaurantDefinitionRepository;
    private final RestaurantCapacityRepository restaurantCapacityRepository;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> all(@RequestParam(value = "addStat",required = false) boolean addStat) {
        List<Restaurant> restaurants = new ArrayList<>();
        List<RestaurantDefinition> definitions = restaurantDefinitionRepository.findAll();
        definitions.parallelStream().forEach(d -> {
            List<RestaurantCapacity> capacities = restaurantCapacityRepository.findAllByRestaurant(d);
            capacities.forEach(c -> c.setRestaurant(null));
            Restaurant restaurant = Restaurant.builder()
                    .definition(d)
                    .capacities(capacities)
                    .build();
            if (addStat) {
                List<RestaurantUsage> usages = getRestaurantUsage(d.getId());
                restaurant.setUsages(usages);
            }
            restaurants.add(restaurant);
        });
        return restaurants;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant byId(@PathVariable("id") long restaurantId) {
        Optional<RestaurantDefinition> definitionOptional = restaurantDefinitionRepository.findById(restaurantId);
        if (!definitionOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(RESTAURANT_DOESNT_EXIST, restaurantId));
        }
        RestaurantDefinition definition = definitionOptional.get();
        Restaurant restaurant = Restaurant.builder()
                .definition(definition)
                .capacities(restaurantCapacityRepository.findAllByRestaurant(definition))
                .build();

        List<RestaurantUsage> usages = getRestaurantUsage(definition.getId());
        restaurant.setUsages(usages);
        return restaurant;
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant save(@RequestBody Restaurant restaurant) {
        if (restaurant == null || restaurant.getDefinition() == null || restaurant.getDefinition().getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMPTY_SITE);
        }
        RestaurantDefinition d = restaurant.getDefinition();

        try {
            sanitizeCapacities(restaurant);
        } catch (AppException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        d = restaurantDefinitionRepository.save(d);
        restaurantCapacityRepository.deleteAllByRestaurant(d);

        List<RestaurantCapacity> updatedCapacities = new ArrayList<>();
        if(restaurant.getCapacities() !=null) {
            for (RestaurantCapacity capacity : restaurant.getCapacities()) {
                capacity.setRestaurant(d);
                updatedCapacities.add(restaurantCapacityRepository.save(capacity));
            }
        }
        return Restaurant.builder().definition(d).capacities(updatedCapacities).build();
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") long siteId) {
        restaurantCapacityRepository.deleteAllByRestaurant(RestaurantDefinition.builder().id(siteId).build());
        restaurantDefinitionRepository.deleteById(siteId);
    }

    private List<RestaurantUsage> getRestaurantUsage(Long siteId) {
        return new ArrayList<>();
    }

    private void sanitizeCapacities(Restaurant restaurant) throws AppException {
        List<RestaurantCapacity> capacities = restaurant.getCapacities();

        if(capacities!=null) {
            List<RestaurantCapacity> updated = new ArrayList<>();
            capacities.sort(Comparator.comparing(RestaurantCapacity::getStartDate));
            LocalDate date = null;
            for (RestaurantCapacity capacity : capacities) {
                if (capacity.getStartDate() == null || capacity.getEndDate() == null) {
                    throw new AppException("bad capacity definition");
                }
                if (date != null && !capacity.getStartDate().isAfter(date)) {
                    capacity.setStartDate(date);
                }
                if(!capacity.getStartDate().isAfter(capacity.getEndDate())) {
                    updated.add(capacity);
                    date = capacity.getEndDate().plusDays(1);
                }
            }
            restaurant.setCapacities(updated);
        }
    }
}
