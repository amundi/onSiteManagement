package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.ParkingDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingDefinitionRepository extends JpaRepository<ParkingDefinition, Long> {

    List<ParkingDefinition> findAllByName(String name);
}
