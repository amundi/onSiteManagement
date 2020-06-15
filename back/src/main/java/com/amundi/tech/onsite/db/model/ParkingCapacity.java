package com.amundi.tech.onsite.db.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class ParkingCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking", nullable = false)
    private ParkingDefinition parking;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "moto")
    private int moto;

    @Column(name = "car")
    private int car;

    @Column(name = "bike")
    private int bike;
}
