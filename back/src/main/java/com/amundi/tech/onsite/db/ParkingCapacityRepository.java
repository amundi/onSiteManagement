package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.ParkingCapacity;
import com.amundi.tech.onsite.db.model.ParkingDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParkingCapacityRepository extends JpaRepository<ParkingCapacity, Long> {

    List<ParkingCapacity> findAllByParking(ParkingDefinition parking);

    @Transactional
    void deleteAllByParking(ParkingDefinition parking);
}
