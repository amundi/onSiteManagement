package com.amundi.tech.onsite.db.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "WORKING_DAY", indexes = {
        @Index(name = "WORKING_DAY_INDEX_USER", columnList = "user"),
        @Index(name = "WORKING_DAY_INDEX_DATE", columnList = "date"),
        @Index(name = "WORKING_DAY_INDEX_SITE", columnList = "site"),
        @Index(name = "WORKING_DAY_INDEX_RESTAURANT", columnList = "restaurant"),
        @Index(name = "WORKING_DAY_INDEX_PARKING", columnList = "parking"),
})
@Data
public class WorkingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user", nullable = false)
    private String user;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    // Site usages ---------------------------
    @ManyToOne
    @JoinColumn(name = "site", nullable = false)
    private SiteDefinition site;

    @Column(name = "arrival")
    private int arrival; // 800, 830, 900, 930, 1000

    @Column(name = "departure")
    private int departure; // 1700, 1730, 1800, 1830, 1900

    @Column(name= "onsite")
    private boolean onSite;

    @Column(name= "offsite")
    private boolean offSite;

    // Restaurant usages ------------------------
    @ManyToOne
    @JoinColumn(name = "restaurant")
    private RestaurantDefinition restaurant;

    @Column(name = "lunch")
    private int lunch; // 1130, 1200, 1230, 1300, 1330

    // Parking usages --------------------------
    @ManyToOne
    @JoinColumn(name = "parking")
    private ParkingDefinition parking;

    @Column(name = "type")
    private String parkingType; //moto, car, bike
}
