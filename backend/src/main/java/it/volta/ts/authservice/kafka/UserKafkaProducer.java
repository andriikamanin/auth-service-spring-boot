package it.volta.ts.authservice.kafka;

import it.volta.ts.authservice.dto.UserRegisteredEvent;
import it.volta.ts.authservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegisteredEvent(User user) {
        var event = new UserRegisteredEvent(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );
        kafkaTemplate.send("user.registered", event);
    }
}
