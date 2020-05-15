package com.amundi.tech.onsite.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {

    private String firstName;
    private String lastName;
    private String login;
    private String organization;
    private String role;

}
