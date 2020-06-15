package com.amundi.tech.onsite.model.mapper;

import com.amundi.tech.onsite.model.usage.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsageMapper {

    SiteUsageImpl getSimpleUsage(SiteUsageReport report);
    RestaurantUsageImpl getSimpleUsage(RestaurantUsageReport report);
    ParkingUsageImpl getSimpleUsage(ParkingUsageReport report);
}