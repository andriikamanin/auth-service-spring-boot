package it.volta.ts.userservice.controller;

import it.volta.ts.userservice.dto.LoginHistoryEntryDto;
import it.volta.ts.userservice.dto.UpdateProfileRequest;
import it.volta.ts.userservice.dto.UserProfileDto;
import it.volta.ts.userservice.service.LoginHistoryService;
import it.volta.ts.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginHistoryService loginHistoryService;

    @GetMapping("/me")
    public UserProfileDto getMyProfile(@AuthenticationPrincipal UUID userId) {
        return userService.getById(userId, userId);
    }

    @PutMapping("/me")
    public void updateMyProfile(@AuthenticationPrincipal UUID userId,
                                @RequestBody @Validated UpdateProfileRequest request) {
        userService.updateProfile(userId, request);
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<String> uploadAvatar(@AuthenticationPrincipal UUID userId,
                                               @RequestParam("file") MultipartFile file) throws IOException {
        String avatarUrl = userService.storeAvatar(userId, file);
        return ResponseEntity.ok(avatarUrl);
    }

    @GetMapping("/{id}")
    public UserProfileDto getProfileById(@PathVariable UUID id,
                                         @AuthenticationPrincipal UUID currentUserId) {
        return userService.getById(id, currentUserId);
    }

    @GetMapping("/nickname/{nickname}")
    public UserProfileDto getByNickname(@PathVariable String nickname) {
        return userService.getByNickname(nickname);
    }

    @GetMapping("/search")
    public List<UserProfileDto> searchByNickname(@RequestParam("query") String query) {
        return userService.searchByNickname(query);
    }

    @GetMapping("/me/logins")
    public List<LoginHistoryEntryDto> getLoginHistory(@AuthenticationPrincipal UUID userId) {
        return loginHistoryService.getRecentLogins(userId);
    }
}