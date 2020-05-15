package com.amundi.tech.onsite.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUsage {
    private LocalDate date;

    private int h1130;
    private int h1200;
    private int h1230;
    private int h1300;
    private int h1330;

    private int h1130_u;
    private int h1200_u;
    private int h1230_u;
    private int h1300_u;
    private int h1330_u;
}
