package it.volta.ts.userservice.kafka;

import it.volta.ts.userservice.dto.UserRegisteredEvent;
import it.volta.ts.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredListener {

    private final UserService userService;

    @KafkaListener(
            topics = "user.registered",
            groupId = "user-service",
            containerFactory = "userRegisteredListenerContainerFactory"
    )
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("📥 Получено событие user.registered: {}", event);
        userService.createProfileFromEvent(event);
    }
}