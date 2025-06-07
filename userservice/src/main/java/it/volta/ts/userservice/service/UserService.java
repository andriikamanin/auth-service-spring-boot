package it.volta.ts.userservice.service;


import java.io.IOException;
import it.volta.ts.userservice.dto.UpdateProfileRequest;
import it.volta.ts.userservice.dto.UserProfileDto;
import it.volta.ts.userservice.dto.UserRegisteredEvent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserProfileDto getById(UUID userId);

    void updateProfile(UUID userId, UpdateProfileRequest request);
    void createProfileFromEvent(UserRegisteredEvent event);
    String storeAvatar(UUID userId, MultipartFile file) throws IOException;
    UserProfileDto getById(UUID userId, UUID currentUserId);
    UserProfileDto getByNickname(String nickname);
    List<UserProfileDto> searchByNickname(String query);
    void updateEmail(UUID userId, String newEmail);
}