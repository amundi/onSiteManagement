
package com.amundi.tech.onsite.model.usage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingUsageReport implements ParkingUsage{
    private LocalDate date;

    private int moto;
    private int car;
    private int bike;

    private Set<String> moto_users;
    private Set<String> car_users;
    private Set<String> bike_users;

    public int getMoto_u() {
        return moto_users.size();
    }

    public int getCar_u() {
        return car_users.size();
    }

    public int getBike_u() {
        return bike_users.size();
    }
}
