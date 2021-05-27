package com.example.timetracker.repository;

import com.example.timetracker.domain.TimeEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimeEntryRepository extends CrudRepository<TimeEntry, UUID> {
}
