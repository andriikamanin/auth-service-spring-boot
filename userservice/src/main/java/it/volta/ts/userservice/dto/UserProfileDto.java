package it.volta.ts.userservice.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private UUID id;
    private String email;
    private String nickname;
    private String bio;
    private String avatarUrl;
    private Set<String> roles;
    private boolean publicProfile;
}