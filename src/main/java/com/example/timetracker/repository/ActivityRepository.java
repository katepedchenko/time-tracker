package com.example.timetracker.repository;

import com.example.timetracker.domain.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, UUID> {

    List<Activity> findByUserId(UUID userId);

    List<Activity> findByUserIdAndDate(UUID userId, LocalDate date);

    Activity findByIdAndUserId(UUID workdayEntryId, UUID userId);
}
