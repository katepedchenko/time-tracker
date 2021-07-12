package com.example.timetracker.repository;

import com.example.timetracker.domain.Project;
import com.example.timetracker.domain.ProjectStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends CrudRepository<Project, UUID> {

    List<Project> findAllByStatus(ProjectStatus status);
}
