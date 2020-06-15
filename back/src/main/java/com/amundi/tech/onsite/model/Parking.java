package com.amundi.tech.onsite.model;

import com.amundi.tech.onsite.db.model.ParkingCapacity;
import com.amundi.tech.onsite.db.model.ParkingDefinition;
import com.amundi.tech.onsite.model.usage.ParkingUsageImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parking {

    public static final String MOTO = "moto";
    public static final String CAR = "car";
    public static final String BIKE = "bike";

    private ParkingDefinition definition;
    private List<ParkingCapacity> capacities;
    private List<ParkingUsageImpl> usages;
}
