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
public class SiteUsageImpl implements SiteUsage {
    private LocalDate date;

    private int h700; // max capacity
    private int h730;
    private int h800;
    private int h830;
    private int h900;
    private int h930;
    private int h1000;
    private int h1030;

    private int h700_u;
    private int h730_u;
    private int h800_u;
    private int h830_u;
    private int h900_u;
    private int h930_u;
    private int h1000_u;
    private int h1030_u;

    private int onSite;
    private int onSite_u;
    private int offSite_u;

    private int h1700;
    private int h1730;
    private int h1800;
    private int h1830;
    private int h1900;

    private int h1700_u;
    private int h1730_u;
    private int h1800_u;
    private int h1830_u;
    private int h1900_u;
}
