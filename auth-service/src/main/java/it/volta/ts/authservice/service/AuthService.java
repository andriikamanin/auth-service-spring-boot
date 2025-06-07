package it.volta.ts.authservice.service;

import it.volta.ts.authservice.config.AppProperties;
import it.volta.ts.authservice.dto.*;
import it.volta.ts.authservice.entity.Role;
import it.volta.ts.authservice.entity.User;
import it.volta.ts.authservice.kafka.UserKafkaProducer;
import it.volta.ts.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final StringRedisTemplate redisTemplate;
    private final AppProperties appProperties;
    private final JwtService jwtService;
    private final UserKafkaProducer userKafkaProducer;

    private static final Duration VERIFICATION_TOKEN_TTL = Duration.ofHours(24);
    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(30);
    private static final Duration RESET_TOKEN_TTL = Duration.ofHours(1);

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email уже используется");
        }

        String nickname = "user-" + UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(nickname)
                .roles(Set.of(Role.ADMIN))
                .build();

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("email:verify:" + token, user.getId().toString(), VERIFICATION_TOKEN_TTL);

        String verifyUrl = appProperties.getFrontendBaseUrl() + "/verify-email?token=" + token;
        String message = "Здравствуйте!\n\nПерейдите по ссылке для подтверждения:\n\n" + verifyUrl;

        mailService.sendEmail(user.getEmail(), "Подтверждение регистрации", message);
    }

    public void verifyEmail(String token) {
        String userIdStr = redisTemplate.opsForValue().get("email:verify:" + token);
        if (userIdStr == null) throw new IllegalArgumentException("Invalid or expired token");

        UUID userId = UUID.fromString(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (user.isEmailVerified()) throw new IllegalStateException("Email is already verified");

        user.setEmailVerified(true);
        userRepository.save(user);
        redisTemplate.delete("email:verify:" + token);
        userKafkaProducer.sendUserRegisteredEvent(user);
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Please verify your email before logging in");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        Map<String, Object> claims = Map.of(
                "roles", user.getRoles().stream().map(Enum::name).toList()
        );

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), claims);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        redisTemplate.opsForValue().set("refresh:" + user.getId(), refreshToken, REFRESH_TOKEN_TTL);

        userKafkaProducer.sendUserLoginEvent(
                UserLoginEvent.builder()
                        .userId(user.getId())
                        .ip(httpRequest.getRemoteAddr())
                        .userAgent(httpRequest.getHeader("User-Agent"))
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        return new LoginResponse(accessToken, refreshToken);
    }

    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        redisTemplate.delete("refresh:" + user.getId());
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("password:reset:" + token, user.getId().toString(), RESET_TOKEN_TTL);

        String resetLink = appProperties.getFrontendBaseUrl() + "/reset-password?token=" + token;
        String message = "Click the link to reset your password:\n\n" + resetLink;

        mailService.sendEmail(user.getEmail(), "Reset your password", message);
    }

    public void resetPassword(String token, String newPassword) {
        String userIdStr = redisTemplate.opsForValue().get("password:reset:" + token);
        if (userIdStr == null) throw new IllegalArgumentException("Invalid or expired token");

        UUID userId = UUID.fromString(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redisTemplate.delete("password:reset:" + token);
    }

    public LoginResponse refreshToken(String token) {
        if (!jwtService.isTokenValid(token)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        UUID userId = jwtService.extractUserId(token);
        String savedToken = redisTemplate.opsForValue().get("refresh:" + userId);

        if (savedToken == null || !savedToken.equals(token)) {
            throw new IllegalStateException("Refresh token not found or does not match");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Map<String, Object> claims = Map.of(
                "roles", user.getRoles().stream().map(Enum::name).toList()
        );

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getEmail(), claims);
        return new LoginResponse(newAccessToken, token);
    }

    public void logout(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        UUID userId = jwtService.extractUserId(refreshToken);
        String redisKey = "refresh:" + userId;

        String savedToken = redisTemplate.opsForValue().get(redisKey);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new IllegalStateException("Refresh token not found or already logged out");
        }

        redisTemplate.delete(redisKey);
    }

    public void sendOldEmailConfirmation(User user) {
        String code = generate6DigitCode();
        redisTemplate.opsForValue().set("email:change:old:" + user.getId(), code, Duration.ofMinutes(10));
        mailService.sendEmail(user.getEmail(), "Подтверждение старой почты", "Код подтверждения: " + code);
    }

    public void verifyOldEmailCode(User user, String code) {
        String stored = redisTemplate.opsForValue().get("email:change:old:" + user.getId());
        if (!code.equals(stored)) throw new IllegalArgumentException("Invalid code");
        redisTemplate.opsForValue().set("email:change:old:verified:" + user.getId(), "true", Duration.ofMinutes(15));
    }

    public void sendNewEmailConfirmation(User user, String newEmail) {
        String verified = redisTemplate.opsForValue().get("email:change:old:verified:" + user.getId());
        if (!"true".equals(verified)) throw new IllegalStateException("Old email not verified");

        String code = generate6DigitCode();
        redisTemplate.opsForValue().set("email:change:new:" + user.getId(), code, Duration.ofMinutes(10));
        redisTemplate.opsForValue().set("email:change:target:" + user.getId(), newEmail, Duration.ofMinutes(10));

        mailService.sendEmail(newEmail, "Подтверждение новой почты", "Код подтверждения: " + code);
    }

    public void verifyNewEmailAndUpdate(User user, String code) {
        String stored = redisTemplate.opsForValue().get("email:change:new:" + user.getId());
        if (!code.equals(stored)) throw new IllegalArgumentException("Invalid code");

        String newEmail = redisTemplate.opsForValue().get("email:change:target:" + user.getId());
        if (newEmail == null) throw new IllegalStateException("Missing new email");

        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setEmail(newEmail);
        user.setEmailVerified(false);
        userRepository.save(user);

        redisTemplate.delete("email:change:old:" + user.getId());
        redisTemplate.delete("email:change:old:verified:" + user.getId());
        redisTemplate.delete("email:change:new:" + user.getId());
        redisTemplate.delete("email:change:target:" + user.getId());

        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("email:verify:" + token, user.getId().toString(), Duration.ofHours(24));
        String verifyUrl = appProperties.getFrontendBaseUrl() + "/verify-email?token=" + token;
        mailService.sendEmail(newEmail, "Подтвердите новую почту", "Ссылка подтверждения:\n\n" + verifyUrl);

        userKafkaProducer.sendUserEmailChangedEvent(user.getId(), newEmail);
    }

    private String generate6DigitCode() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }
}
