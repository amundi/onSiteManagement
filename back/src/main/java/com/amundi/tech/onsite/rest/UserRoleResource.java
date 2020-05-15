package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.UserRoleRepository;
import com.amundi.tech.onsite.db.model.UserRole;
import com.amundi.tech.onsite.model.UserRoles;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.amundi.tech.onsite.exception.AppException.CURRENT_USER_CANNOT_REMOVE_ADMIN_ROLE;
import static com.amundi.tech.onsite.exception.AppException.EMPTY_USER_ROLES;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
@Tag(name = "userRole", description = "manage role (IE admin) list")
public class UserRoleResource {

    private final UserRoleRepository userRoleRepository;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRoles all() {
        return UserRoles.builder().userRoles(userRoleRepository.findAll()).build();
    }

    @PostMapping(path = "/all",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRoles updateAll(@RequestBody UserRoles userRoles) {
        if (userRoles == null || userRoles.getUserRoles() == null || userRoles.getUserRoles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, EMPTY_USER_ROLES);
        }
        if (!currentUserIsAdmin(userRoles.getUserRoles())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, CURRENT_USER_CANNOT_REMOVE_ADMIN_ROLE);
        }
        userRoleRepository.deleteAll();
        return UserRoles.builder().userRoles(userRoleRepository.saveAll(userRoles.getUserRoles())).build();
    }

    private boolean currentUserIsAdmin(List<UserRole> userRoles) {
        //TODO
        return true;
    }
}
