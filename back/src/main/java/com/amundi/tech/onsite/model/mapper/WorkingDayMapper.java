package com.amundi.tech.onsite.model.mapper;

import com.amundi.tech.onsite.db.model.WorkingDay;
import com.amundi.tech.onsite.model.UserDay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WorkingDayMapper {

    @Mappings({
            @Mapping(target = "siteId", source = "site.id"),
            @Mapping(target = "parkingId", source = "parking.id"),
            @Mapping(target = "restaurantId", source = "restaurant.id"),
    })
    UserDay convert(WorkingDay d);

    @Mappings({
            @Mapping(target = "site.id", source = "siteId"),
            @Mapping(target = "parking.id", source = "parkingId"),
            @Mapping(target = "restaurant.id", source = "restaurantId"),
    })
    WorkingDay convert(UserDay d);
}