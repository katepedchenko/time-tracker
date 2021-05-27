package com.example.timetracker.repository;

import com.example.timetracker.domain.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, UUID> {
}
