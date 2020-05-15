package com.amundi.tech.onsite.model;

import com.amundi.tech.onsite.db.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoles {
    private List<UserRole> userRoles;
}
