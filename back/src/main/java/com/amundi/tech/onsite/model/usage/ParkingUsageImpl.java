package com.amundi.tech.onsite.model.usage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingUsageImpl implements ParkingUsage {
    private LocalDate date;

    private int moto;
    private int car;
    private int bike;
    private int moto_u;
    private int car_u;
    private int bike_u;
}
