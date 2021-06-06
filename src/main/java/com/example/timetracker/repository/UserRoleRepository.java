package com.example.timetracker.repository;

import com.example.timetracker.domain.UserRole;
import com.example.timetracker.domain.UserRoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, UUID> {

    UserRole findByType(UserRoleType roleType);
}
