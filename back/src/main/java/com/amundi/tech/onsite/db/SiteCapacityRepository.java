package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.SiteCapacity;
import com.amundi.tech.onsite.db.model.SiteDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SiteCapacityRepository extends JpaRepository<SiteCapacity, Long> {

    List<SiteCapacity> findAllBySite(SiteDefinition site);

    @Transactional
    void deleteAllBySite(SiteDefinition site);
}
