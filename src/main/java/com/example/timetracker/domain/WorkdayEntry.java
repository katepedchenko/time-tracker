package com.example.timetracker.domain;

import com.example.timetracker.repository.converter.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class WorkdayEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime startedAt;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private EntryStatus status;

    @ManyToOne
    private AppUser user;

    @OneToMany(mappedBy = "workdayEntry", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<TimeEntry> timeEntries = new ArrayList<>();
}
