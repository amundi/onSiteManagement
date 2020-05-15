package com.amundi.tech.onsite;

import com.amundi.tech.onsite.db.UserRoleRepository;
import com.amundi.tech.onsite.db.model.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class AppInitializer {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @PostConstruct
    private void init() {
        if(userRoleRepository.findAll().isEmpty()) {
            log.info("Creating default admin user: gazin");
            UserRole userRole = new UserRole();
            userRole.setUser("gazin");
            userRole.setRole("admin");
            userRoleRepository.save(userRole);
        }
    }
}