package com.example.timetracker.repository;

import com.example.timetracker.domain.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, UUID> {

    List<Activity> findByUserId(UUID userId);

    Activity findByIdAndUserId(UUID workdayEntryId, UUID userId);

    List<Activity> findByUserIdAndFinishedAtIsNull(UUID userId);
}
