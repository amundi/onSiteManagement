package com.amundi.tech.onsite.model.usage;

import java.time.LocalDate;

public interface RestaurantUsage {
    LocalDate getDate();

    int getH1130();   // max capacity
    int getH1200();
    int getH1230();
    int getH1300();
    int getH1330();

    int getH1130_u(); // _u => used
    int getH1200_u();
    int getH1230_u();
    int getH1300_u();
    int getH1330_u();
}
