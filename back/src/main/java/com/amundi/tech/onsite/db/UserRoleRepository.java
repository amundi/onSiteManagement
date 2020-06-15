package com.amundi.tech.onsite.db;

import com.amundi.tech.onsite.db.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> getAllByUser(String user);

    List<UserRole> getAllByRoleNotIn(List<String> excludedRoles);

    @Transactional
    void deleteAllByUser(String user);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update UserRole u set u.role = :role where u.user = :user")
    int updateRole(@Param("role") String role, @Param("user") String user);

}
