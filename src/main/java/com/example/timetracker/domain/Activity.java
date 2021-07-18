package com.example.timetracker.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Activity {

    @Id
    @GeneratedValue
    private UUID id;

    private Integer hours;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    private String description;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Project project;
}
