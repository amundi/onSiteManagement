package com.amundi.tech.onsite.db.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class RestaurantCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant", nullable = false)
    private RestaurantDefinition restaurant;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    // Arrivals -----------------------------
    @Column(name = "arrival_1130")
    private int arrival_1130;

    @Column(name = "arrival_1200")
    private int arrival_1200;

    @Column(name = "arrival_1230")
    private int arrival_1230;

    @Column(name = "arrival_1300")
    private int arrival_1300;

    @Column(name = "arrival_1330")
    private int arrival_1330;
}
