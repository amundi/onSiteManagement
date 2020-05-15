package com.amundi.tech.onsite.util;

import com.amundi.tech.onsite.model.SimpleSiteUsage;
import com.amundi.tech.onsite.model.SiteReport;

public class ModelUtils {
    public static SimpleSiteUsage getFromSiteReport(SiteReport r) {
        if(r!=null) {
            return SimpleSiteUsage.builder()
                    .date(r.getDate())
                    .h700(r.getH700())
                    .h730(r.getH730())
                    .h800(r.getH800())
                    .h830(r.getH830())
                    .h900(r.getH900())
                    .h930(r.getH930())
                    .h1000(r.getH1000())
                    .h1030(r.getH1030())
                    .onSite(r.getOnSite())
                    .h700_u(r.getH700_u())
                    .h730_u(r.getH730_u())
                    .h800_u(r.getH800_u())
                    .h830_u(r.getH830_u())
                    .h900_u(r.getH900_u())
                    .h930_u(r.getH930_u())
                    .h1000_u(r.getH1000_u())
                    .h1030_u(r.getH1030_u())
                    .onSite_u(r.getOnSite_u())
                    .h1130(r.getH1130())
                    .h1200(r.getH1200())
                    .h1230(r.getH1230())
                    .h1300(r.getH1300())
                    .h1330(r.getH1330())
                    .h1130_u(r.getH1130_u())
                    .h1200_u(r.getH1200_u())
                    .h1230_u(r.getH1230_u())
                    .h1300_u(r.getH1300_u())
                    .h1330_u(r.getH1330_u())
                    .h1700(r.getH1700())
                    .h1730(r.getH1730())
                    .h1800(r.getH1800())
                    .h1830(r.getH1830())
                    .h1900(r.getH1900())
                    .h1700_u(r.getH1700_u())
                    .h1730_u(r.getH1730_u())
                    .h1800_u(r.getH1800_u())
                    .h1830_u(r.getH1830_u())
                    .h1900_u(r.getH1900_u())
                    .car(r.getCar())
                    .moto(r.getMoto())
                    .car_u(r.getCar_u())
                    .moto_u(r.getMoto_u()).build();
        }
        return null;
    }
}
