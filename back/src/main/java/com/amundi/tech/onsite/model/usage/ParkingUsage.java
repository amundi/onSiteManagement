package com.amundi.tech.onsite.model.usage;

import java.time.LocalDate;

public interface ParkingUsage {
    LocalDate getDate();

    int getMoto();
    int getMoto_u();

    int getCar();
    int getCar_u();

    int getBike();
    int getBike_u();

}
