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
public class RestaurantUsageReport implements RestaurantUsage {
    private LocalDate date;

    private int h1130;
    private int h1200;
    private int h1230;
    private int h1300;
    private int h1330;

    private Set<String> h1130_users;
    private Set<String> h1200_users;
    private Set<String> h1230_users;
    private Set<String> h1300_users;
    private Set<String> h1330_users;

    public int getH1130_u() {
        return h1130_users.size();
    }

    public int getH1200_u() {
        return h1200_users.size();
    }

    public int getH1230_u() {
        return h1230_users.size();
    }

    public int getH1300_u() {
        return h1300_users.size();
    }

    public int getH1330_u() {
        return h1330_users.size();
    }
}
