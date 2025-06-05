package it.volta.ts.authservice.service;


import it.volta.ts.authservice.config.AppProperties;
import it.volta.ts.authservice.dto.RegisterRequest;
import it.volta.ts.authservice.entity.User;
import it.volta.ts.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final StringRedisTemplate redisTemplate;
    private final AppProperties appProperties;

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
}