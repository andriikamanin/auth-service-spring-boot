package it.volta.ts.userservice.kafka;

import it.volta.ts.userservice.dto.UserLoginEvent;
import it.volta.ts.userservice.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLoginListener {

    private final LoginHistoryService loginHistoryService;

    @KafkaListener(
            topics = "user.login",
            groupId = "user-service",
            containerFactory = "userLoginListenerContainerFactory"
    )
    public void handleUserLogin(UserLoginEvent event) {
        log.info("📥 Получено событие user.login: {}", event);
        loginHistoryService.saveLoginEvent(event);
    }
}