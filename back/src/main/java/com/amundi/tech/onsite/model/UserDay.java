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
public class UserDay {

    private String user;
    private LocalDate date;

    private Long siteId;
    private int arrival;
    private int departure;
    private boolean onSite;
    private boolean offSite;

    private Long restaurantId;
    private int lunch;

    private Long parkingId;
    private String parkingType;
}
