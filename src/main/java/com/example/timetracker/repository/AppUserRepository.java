package com.example.timetracker.repository;

import com.example.timetracker.domain.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, UUID> {

    List<AppUser> findAll();

    boolean existsByExternalId(String externalId);
}
