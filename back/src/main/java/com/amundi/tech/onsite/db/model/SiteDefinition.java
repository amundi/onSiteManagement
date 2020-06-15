package com.amundi.tech.onsite.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private String tags;

    @ManyToOne
    @JoinColumn(name = "default_restaurant")
    private RestaurantDefinition defaultRestaurant;

    @ManyToOne
    @JoinColumn(name = "default_parking")
    private ParkingDefinition defaultParking;
}
