package com.amundi.tech.onsite.model;

import com.amundi.tech.onsite.db.model.SiteCapacity;
import com.amundi.tech.onsite.db.model.SiteDefinition;
import com.amundi.tech.onsite.model.usage.SiteUsageImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Site {

    private SiteDefinition definition;
    private List<SiteCapacity> capacities;
    private List<SiteUsageImpl> usages;
}
