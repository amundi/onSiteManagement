package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.UserRoleRepository;
import com.amundi.tech.onsite.db.model.UserRole;
import com.amundi.tech.onsite.model.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "userInfo", description = "retrieve user infos, and user list")
public class UserInfoResource {

    private static final String ADMIN_ROLE = "admin";
    private final UserRoleRepository userRoleRepository;

    @GetMapping(path = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserInfo userInfo() {

        return UserInfo.builder()
                .firstName("Chuck")
                .lastName("Norris")
                .login("norris")
                .organization("ChuckNorris Inc")
                .role(getRole(userRoleRepository.getAllByUser("norris")))
                .build();
    }

    private String getRole(List<UserRole> userRoles) {
        String role = null;
        for(UserRole userRole: userRoles) {
            role = (role == ADMIN_ROLE )? ADMIN_ROLE: userRole.getRole();
        }
        return role;
    }
}
