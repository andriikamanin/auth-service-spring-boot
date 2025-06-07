package it.volta.ts.userservice.kafka;

import it.volta.ts.userservice.dto.UserEmailChangedEvent;
import it.volta.ts.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEmailChangedListener {

    private final UserService userService;

    @KafkaListener(
            topics = "user.emailChanged",
            groupId = "user-service",
            containerFactory = "userEmailChangedListenerContainerFactory"
    )
    public void handleUserEmailChanged(UserEmailChangedEvent event) {
        log.info("📩 Получено событие user.emailChanged: {}", event);

        try {
            userService.updateEmail(event.getUserId(), event.getNewEmail());
            log.info("✅ Email обновлён для пользователя {}", event.getUserId());
        } catch (Exception e) {
            log.error("❌ Ошибка обновления email для пользователя {}: {}", event.getUserId(), e.getMessage(), e);
        }
    }
}