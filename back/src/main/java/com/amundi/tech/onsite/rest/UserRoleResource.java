package com.amundi.tech.onsite.rest;

import com.amundi.tech.onsite.db.UserRoleRepository;
import com.amundi.tech.onsite.db.model.UserRole;
import com.amundi.tech.onsite.exception.AppException;
import com.amundi.tech.onsite.model.UserRoles;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
@Tag(name = "userRole", description = "manage role (IE admin) list")
public class UserRoleResource {

    private final UserRoleRepository userRoleRepository;
    private final String NONE = "none";

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRoles all() {
        return UserRoles.builder().userRoles(userRoleRepository.getAllByRoleNotIn(Arrays.asList(NONE))).build();
    }

    @PostMapping(path = "/all",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRoles updateAll(@RequestBody UserRoles userRoles) {
        if (userRoles == null || userRoles.getUserRoles() == null || userRoles.getUserRoles().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, AppException.EMPTY_USER_ROLES);
        }
        if (!currentUserIsAdmin(userRoles.getUserRoles())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, AppException.CURRENT_USER_CANNOT_REMOVE_ADMIN_ROLE);
        }
        List<UserRole> currents = userRoleRepository.findAll();
        Set<String> users = new HashSet<>();
        for(UserRole current: currents) {
            userRoleRepository.updateRole(NONE, current.getUser());
            users.add(current.getUser());
        }

        for(UserRole newUserRole: userRoles.getUserRoles()) {
            if(users.contains(newUserRole.getUser())) {
                userRoleRepository.updateRole(newUserRole.getRole(), newUserRole.getUser());
            } else {
                userRoleRepository.save(newUserRole);
            }
        }

        return UserRoles.builder().userRoles(userRoleRepository.getAllByRoleNotIn(Arrays.asList(NONE))).build();
    }

    private boolean currentUserIsAdmin(List<UserRole> userRoles) {
        //TODO
        return true;
    }
}
