package com.amundi.tech.onsite.model;

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
    void setH700(int h700);
    void setH730(int h730);
    void setH800(int h800);
    void setH830(int h830);
    void setH900(int h900);
    void setH930(int h930);
    void setH1000(int h1000);
    void setH1030(int h1030);

    int getOnSite();
    void setOnSite(int onSite);

    int getH700_u(); // _u => used
    int getH730_u();
    int getH800_u();
    int getH830_u();
    int getH900_u();
    int getH930_u();
    int getH1000_u();
    int getH1030_u();

    int getOnSite_u();

    int getH1130();
    int getH1200();
    int getH1230();
    int getH1300();
    int getH1330();
    void setH1130(int h1130);
    void setH1200(int h1200);
    void setH1230(int h1230);
    void setH1300(int h1300);
    void setH1330(int h1330);

    int getH1130_u();
    int getH1200_u();
    int getH1230_u();
    int getH1300_u();
    int getH1330_u();

    int getH1700();
    int getH1730();
    int getH1800();
    int getH1830();
    int getH1900();
    void setH1700(int h1700);
    void setH1730(int h1730);
    void setH1800(int h1800);
    void setH1830(int h1830);
    void setH1900(int h1900);

    int getH1700_u();
    int getH1730_u();
    int getH1800_u();
    int getH1830_u();
    int getH1900_u();

    int getMoto();
    int getCar();
    void setMoto(int moto);
    void setCar(int car);

    int getMoto_u();
    int getCar_u();
}
