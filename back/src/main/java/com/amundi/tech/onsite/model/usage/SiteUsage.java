package com.amundi.tech.onsite.model.usage;

import java.time.LocalDate;

public interface SiteUsage {
    LocalDate getDate();

    int getH700();   // max capacity
    int getH730();
    int getH800();
    int getH830();
    int getH900();
    int getH930();
    int getH1000();
    int getH1030();

    int getOnSite();

    int getH700_u(); // _u => used
    int getH730_u();
    int getH800_u();
    int getH830_u();
    int getH900_u();
    int getH930_u();
    int getH1000_u();
    int getH1030_u();

    int getOnSite_u();
    int getOffSite_u();

    int getH1700();
    int getH1730();
    int getH1800();
    int getH1830();
    int getH1900();

    int getH1700_u();
    int getH1730_u();
    int getH1800_u();
    int getH1830_u();
    int getH1900_u();
}
