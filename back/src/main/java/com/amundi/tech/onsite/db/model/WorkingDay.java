package com.amundi.tech.onsite.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "WORKING_DAY", indexes = {
        @Index(name = "WORKING_DAY_INDEX_USER", columnList = "user"),
        @Index(name = "WORKING_DAY_INDEX_SITE", columnList = "site"),
        @Index(name = "WORKING_DAY_INDEX_DATE", columnList = "date"),
        @Index(name = "WORKING_DAY_INDEX_RESTAURANT", columnList = "restaurant"),
})
@Data
public class WorkingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user", nullable = false)
    private String user;

    @ManyToOne
    @JoinColumn(name = "site", nullable = false)
    private SiteDefinition site;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "arrival")
    private int arrival; // 800, 830, 900, 930, 1000

    @Column(name= "onsite")
    private boolean onSite;

    @Column(name = "moto")
    private boolean moto;

    @Column(name = "car")
    private boolean car;

    @Column(name = "restaurant")
    private int restaurant; // 1130, 1200, 1230, 1300, 1330

    @Column(name = "departure", nullable = false)
    private int departure; // 1700, 1730, 1800, 1830, 1900

    @JsonInclude
    @Transient
    private String status;

}
