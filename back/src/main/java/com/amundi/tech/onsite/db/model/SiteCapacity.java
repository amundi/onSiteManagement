package com.amundi.tech.onsite.db.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class SiteCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site", nullable = false)
    private SiteDefinition site;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    // Arrivals -----------------------------
    @Column(name = "arrival_700")
    private int arrival_700;

    @Column(name = "arrival_730")
    private int arrival_730;

    @Column(name = "arrival_800")
    private int arrival_800;

    @Column(name = "arrival_830")
    private int arrival_830;

    @Column(name = "arrival_900")
    private int arrival_900;

    @Column(name = "arrival_930")
    private int arrival_930;

    @Column(name = "arrival_1000")
    private int arrival_1000;

    @Column(name = "arrival_1030")
    private int arrival_1030;

    // OnSite
    @Column(name = "onsite")
    private int onSite;

    // Departure ---------------------------
    @Column(name = "departure_1700")
    private int departure_1700;

    @Column(name = "departure_1730")
    private int departure_1730;

    @Column(name = "departure_1800")
    private int departure_1800;

    @Column(name = "departure_1830")
    private int departure_1830;

    @Column(name = "departure_1900")
    private int departure_1900;
}
