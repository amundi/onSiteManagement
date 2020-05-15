package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.RestaurantCapacity;
import com.amundi.tech.onsite.db.model.RestaurantDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RestaurantCapacityRepository extends JpaRepository<RestaurantCapacity, Long> {
    List<RestaurantCapacity> findAllByRestaurant(RestaurantDefinition restaurant);

    @Transactional
    void deleteAllByRestaurant(RestaurantDefinition restaurant);
}
