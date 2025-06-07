package it.volta.ts.userservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 3, max = 30)
    private String nickname;

    @Size(max = 255)
    private String bio;

    private String avatarUrl;

    private Boolean publicProfile; // новое поле для управления видимостью профиля
}