package com.amundi.tech.onsite.model;

import com.amundi.tech.onsite.db.model.RestaurantCapacity;
import com.amundi.tech.onsite.db.model.RestaurantDefinition;
import com.amundi.tech.onsite.model.usage.RestaurantUsageImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    private RestaurantDefinition definition;
    private List<RestaurantCapacity> capacities;
    private List<RestaurantUsageImpl> usages;
}
