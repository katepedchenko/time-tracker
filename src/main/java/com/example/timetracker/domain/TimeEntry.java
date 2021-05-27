package com.example.timetracker.domain;

import com.example.timetracker.repository.converter.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class TimeEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime startedAt;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private TimeEntryStatus status;

    @ManyToOne
    private AppUser user;
}