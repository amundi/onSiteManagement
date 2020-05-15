package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.SiteDefinition;
import com.amundi.tech.onsite.db.model.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
    List<WorkingDay> findAllBySite(SiteDefinition site);
    List<WorkingDay> findAllBySiteAndDateAfter(SiteDefinition site, LocalDate date);
    List<WorkingDay> findAllByUser(String user);
    List<WorkingDay> findAllByUserAndDateAfter(String user, LocalDate date);
    List<WorkingDay> findAllByDateAfterAndDateBefore(LocalDate startDate, LocalDate endDate);

    @Transactional
    void deleteAllBySiteIsNotAndUser(SiteDefinition site, String user);

    @Transactional
    void deleteAllBySiteAndUserAndAndDateEquals(SiteDefinition site, String user, LocalDate date);

    @Transactional
    void deleteAllByUser(String user);
}
