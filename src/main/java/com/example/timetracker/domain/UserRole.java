package com.example.timetracker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserRole {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserRoleType type;

    @ManyToMany(mappedBy = "userRoles")
    private List<AppUser> users = new ArrayList<>();
}