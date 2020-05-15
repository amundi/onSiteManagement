package com.amundi.tech.onsite.db.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"user", "role"})
)
public class UserRole {

    @Id
    @Column(name = "user")
    private String user;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "department")
    private String department;

    @Column(name = "role", nullable = false)
    private String role;
}
