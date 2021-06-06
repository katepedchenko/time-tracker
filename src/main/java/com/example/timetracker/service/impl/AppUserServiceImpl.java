package com.example.timetracker.service.impl;

import com.example.timetracker.domain.AppUser;
import com.example.timetracker.domain.UserRoleType;
import com.example.timetracker.dto.AppUserCreateDTO;
import com.example.timetracker.dto.AppUserReadDTO;
import com.example.timetracker.dto.AppUserUpdateDTO;
import com.example.timetracker.exception.EntityAlreadyExistsException;
import com.example.timetracker.exception.EntityNotFoundException;
import com.example.timetracker.repository.AppUserRepository;
import com.example.timetracker.repository.UserRoleRepository;
import com.example.timetracker.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TranslationService translationService;

    @Override
    public AppUserReadDTO getUserById(UUID id) {
        AppUser user = getUserRequired(id);
        return translationService.translate(user, AppUserReadDTO.class);
    }

    @Override
    public List<AppUserReadDTO> getAllUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return translationService.translateList(users, AppUserReadDTO.class);
    }

    @Override
    public AppUserReadDTO createUser(AppUserCreateDTO createDTO) {
        if (appUserRepository.existsByExternalId(createDTO.getExternalId())) {
            throw new EntityAlreadyExistsException(AppUser.class, createDTO.getExternalId());
        }

        AppUser user = translationService.translate(createDTO, AppUser.class);
        user.getUserRoles().add(userRoleRepository.findByType(UserRoleType.USER));
        user.setIsBlocked(Boolean.FALSE);
        user = appUserRepository.save(user);

        return translationService.translate(user, AppUserReadDTO.class);
    }

    @Override
    public AppUserReadDTO updateUser(UUID id, AppUserUpdateDTO updateDTO) {
        AppUser user = getUserRequired(id);

        translationService.map(updateDTO, user);
        user = appUserRepository.save(user);

        return translationService.translate(user, AppUserReadDTO.class);
    }

    @Override
    public AppUserReadDTO blockUserById(UUID id) {
        AppUser user = getUserRequired(id);

        user.setIsBlocked(Boolean.TRUE);
        user = appUserRepository.save(user);

        return translationService.translate(user, AppUserReadDTO.class);
    }

    @Override
    public AppUserReadDTO unblockUserById(UUID id) {
        AppUser user = getUserRequired(id);

        user.setIsBlocked(Boolean.FALSE);
        user = appUserRepository.save(user);

        return translationService.translate(user, AppUserReadDTO.class);
    }

    private AppUser getUserRequired(UUID id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
    }
}
