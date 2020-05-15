package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, String> {
}
