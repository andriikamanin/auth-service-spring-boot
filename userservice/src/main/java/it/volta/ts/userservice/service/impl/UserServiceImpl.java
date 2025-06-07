package it.volta.ts.userservice.service.impl;

import it.volta.ts.userservice.dto.UpdateProfileRequest;
import it.volta.ts.userservice.dto.UserProfileDto;
import it.volta.ts.userservice.dto.UserRegisteredEvent;
import it.volta.ts.userservice.entity.UserProfile;
import it.volta.ts.userservice.repository.UserProfileRepository;
import it.volta.ts.userservice.service.S3Service;
import it.volta.ts.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;
    private final S3Service s3Service;
    private final RedisTemplate<String, UserProfileDto> redisTemplate; // ✅ Добавлено

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getById(UUID requestedUserId, UUID currentUserId) {
        UserProfile profile = userProfileRepository.findById(requestedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean isOwnerOrPublic = currentUserId.equals(requestedUserId) || profile.isPublicProfile();
        return mapToDto(profile, isOwnerOrPublic);
    }

    @Override
    public UserProfileDto getById(UUID userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDto(profile, true);
    }

    @Override
    @Transactional
    public void updateProfile(UUID userId, UpdateProfileRequest request) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getNickname() != null) profile.setNickname(request.getNickname());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getAvatarUrl() != null) profile.setAvatarUrl(request.getAvatarUrl());
        if (request.getPublicProfile() != null) profile.setPublicProfile(request.getPublicProfile());

        userProfileRepository.save(profile);

        // 🔄 Обновить кэш, если никнейм изменился
        String redisKey = "profile:nickname:" + profile.getNickname().toLowerCase();
        redisTemplate.opsForValue().set(redisKey, mapToDto(profile, false), Duration.ofMinutes(10));
    }

    @Override
    @Transactional
    public String storeAvatar(UUID userId, MultipartFile file) throws IOException {
        String avatarUrl = s3Service.uploadAvatar(userId, file);

        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        profile.setAvatarUrl(avatarUrl);
        userProfileRepository.save(profile);

        return avatarUrl;
    }

    @Override
    @Transactional
    public void createProfileFromEvent(UserRegisteredEvent event) {
        if (userProfileRepository.existsById(event.id())) {
            log.warn("Профиль уже существует для userId: {}", event.id());
            return;
        }

        UserProfile profile = new UserProfile();
        profile.setId(event.id());
        profile.setEmail(event.email());
        profile.setNickname(event.nickname());
        profile.setRoles(event.roles());
        profile.setPublicProfile(true); // по умолчанию публичный
        userProfileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getByNickname(String nickname) {
        String redisKey = "profile:nickname:" + nickname.toLowerCase();
        UserProfileDto cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            log.debug("🔁 Кешированное значение найдено для nickname: {}", nickname);
            return cached;
        }

        UserProfile profile = userProfileRepository.findByNicknameIgnoreCase(nickname)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!profile.isPublicProfile()) {
            throw new AccessDeniedException("Profile is not public");
        }

        UserProfileDto dto = mapToDto(profile, false);
        redisTemplate.opsForValue().set(redisKey, dto, Duration.ofMinutes(10));
        return dto;
    }

    private UserProfileDto mapToDto(UserProfile profile, boolean isOwnerOrPublic) {
        return UserProfileDto.builder()
                .id(profile.getId())
                .email(isOwnerOrPublic ? profile.getEmail() : null)
                .nickname(profile.getNickname())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .roles(isOwnerOrPublic ? profile.getRoles() : null)
                .publicProfile(profile.isPublicProfile())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> searchByNickname(String query) {
        return userProfileRepository.findAllByNicknameContainingIgnoreCase(query).stream()
                .filter(UserProfile::isPublicProfile)
                .map(profile -> mapToDto(profile, false))
                .toList();
    }


    @Override
    @Transactional
    public void updateEmail(UUID userId, String newEmail) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        profile.setEmail(newEmail);
        userProfileRepository.save(profile);

        // 🔄 Обновление кэша, если профиль есть в Redis
        String redisKey = "profile:nickname:" + profile.getNickname().toLowerCase();
        redisTemplate.opsForValue().set(redisKey, mapToDto(profile, false), Duration.ofMinutes(10));

        log.info("📧 Email updated for user {} -> {}", userId, newEmail);
    }
}