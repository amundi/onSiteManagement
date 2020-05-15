package com.amundi.tech.onsite.model;

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
public class SiteReport implements SiteUsage {
    private LocalDate date;

    private int h700; // max capacity
    private int h730;
    private int h800;
    private int h830;
    private int h900;
    private int h930;
    private int h1000;
    private int h1030;

    private Set<String> h700_users;
    private Set<String> h730_users;
    private Set<String> h800_users;
    private Set<String> h830_users;
    private Set<String> h900_users;
    private Set<String> h930_users;
    private Set<String> h1000_users;
    private Set<String> h1030_users;

    @Override
    public int getH700_u() {
        return h700_users.size();
    }

    @Override
    public int getH730_u() {
        return h730_users.size();
    }

    @Override
    public int getH800_u() {
        return h800_users.size();
    }

    @Override
    public int getH830_u() {
        return h830_users.size();
    }

    @Override
    public int getH900_u() {
        return h900_users.size();
    }

    @Override
    public int getH930_u() {
        return h930_users.size();
    }

    @Override
    public int getH1000_u() {
        return h1000_users.size();
    }

    @Override
    public int getH1030_u() {
        return h1030_users.size();
    }


    private int onSite;
    private Set<String> onSite_users;

    @Override
    public int getOnSite_u() {
        return onSite_users.size();
    }

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

    @Override
    public int getH1130_u() {
        return h1130_users.size();
    }

    @Override
    public int getH1200_u() {
        return h1200_users.size();
    }

    @Override
    public int getH1230_u() {
        return h1230_users.size();
    }

    @Override
    public int getH1300_u() {
        return h1300_users.size();
    }

    @Override
    public int getH1330_u() {
        return h1330_users.size();
    }

    private int h1700;
    private int h1730;
    private int h1800;
    private int h1830;
    private int h1900;

    private Set<String> h1700_users;
    private Set<String> h1730_users;
    private Set<String> h1800_users;
    private Set<String> h1830_users;
    private Set<String> h1900_users;

    @Override
    public int getH1700_u() {
        return h1700_users.size();
    }

    @Override
    public int getH1730_u() {
        return h1730_users.size();
    }

    @Override
    public int getH1800_u() {
        return h1800_users.size();
    }

    @Override
    public int getH1830_u() {
        return h1830_users.size();
    }

    @Override
    public int getH1900_u() {
        return h1900_users.size();
    }

    private int moto;
    private int car;

    private Set<String> moto_users;
    private Set<String> car_users;

    @Override
    public int getMoto_u() {
        return moto_users.size();
    }

    @Override
    public int getCar_u() {
        return car_users.size();
    }
}
