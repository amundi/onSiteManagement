package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.SiteDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteDefinitionRepository extends JpaRepository<SiteDefinition, Long> {

    List<SiteDefinition> findAllByName(String name);
}
