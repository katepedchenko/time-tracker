package com.example.timetracker.repository;

import com.example.timetracker.domain.WorkdayEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkdayEntryRepository extends CrudRepository<WorkdayEntry, UUID> {

    List<WorkdayEntry> findByUserId(UUID userId);

    WorkdayEntry findByIdAndUserId(UUID workdayEntryId, UUID userId);

    List<WorkdayEntry> findByUserIdAndFinishedAtIsNull(UUID userId);
}
