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
public class AppUser {

    @Id
    @GeneratedValue
    private UUID id;

    private String externalId;

    private String fullName;

    @OneToMany(mappedBy = "user")
    private List<TimeEntry> timeEntries = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id"))
    private List<UserRole> userRoles = new ArrayList<>();
}