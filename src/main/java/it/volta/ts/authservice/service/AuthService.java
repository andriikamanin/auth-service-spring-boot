package it.volta.ts.authservice.service;


import it.volta.ts.authservice.config.AppProperties;
import it.volta.ts.authservice.dto.LoginRequest;
import it.volta.ts.authservice.dto.LoginResponse;
import it.volta.ts.authservice.dto.RegisterRequest;
import it.volta.ts.authservice.entity.User;
import it.volta.ts.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final StringRedisTemplate redisTemplate;
    private final AppProperties appProperties;
    private final JwtService jwtService;

    private static final Duration VERIFICATION_TOKEN_TTL = Duration.ofHours(24);

    public void register(RegisterRequest request) {
        // 1. Проверка существующего email
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email уже используется");
        }

        // 2. Генерация никнейма
        String nickname = "user-" + UUID.randomUUID().toString().substring(0, 8);

        // 3. Создание пользователя
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(nickname)
                .build();

        userRepository.save(user);

        // 4. Генерация токена
        String token = UUID.randomUUID().toString();
        String redisKey = "email:verify:" + token;

        // 5. Сохранение токена в Redis с TTL
        redisTemplate.opsForValue().set(redisKey, user.getId().toString(), VERIFICATION_TOKEN_TTL);

        // 6. Генерация ссылки с dynamic base-url
        String verifyUrl = appProperties.getBackendBaseUrl() + "/api/auth/verify?token=" + token;
        String message = "Здравствуйте!\n\nПерейдите по ссылке, чтобы подтвердить регистрацию:\n\n" + verifyUrl;

        // 7. Отправка письма
        mailService.sendEmail(user.getEmail(), "Подтверждение регистрации", message);
    }


    public void verifyEmail(String token) {
        String redisKey = "email:verify:" + token;
        String userIdStr = redisTemplate.opsForValue().get(redisKey);

        if (userIdStr == null) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        UUID userId = UUID.fromString(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (user.isEmailVerified()) {
            throw new IllegalStateException("Email is already verified");
        }

        user.setEmailVerified(true);
        userRepository.save(user);
        redisTemplate.delete(redisKey); // cleanup
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!user.isEmailVerified()) {
            throw new IllegalStateException("Please verify your email before logging in");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Преобразуем Set<Role> в List<String>
        var roleNames = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getEmail(),
                Map.of("roles", roleNames)
        );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        String redisKey = "refresh:" + user.getId();
        redisTemplate.opsForValue().set(redisKey, refreshToken, Duration.ofDays(30));

        return new LoginResponse(accessToken, refreshToken);
    }
}