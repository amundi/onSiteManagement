package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> getAllByUser(String user);

    @Transactional
    void deleteAllByUser(String user);
}
