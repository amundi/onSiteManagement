package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.RestaurantDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantDefinitionRepository extends JpaRepository<RestaurantDefinition, Long> {
    List<RestaurantDefinition> findAllByName(String name);
}
