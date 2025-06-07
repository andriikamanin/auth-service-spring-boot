package it.volta.ts.userservice.service;

import it.volta.ts.userservice.dto.LoginHistoryEntryDto;
import it.volta.ts.userservice.dto.UserLoginEvent;
import it.volta.ts.userservice.entity.LoginHistoryEntry;
import it.volta.ts.userservice.repository.LoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository repository;

    public List<LoginHistoryEntryDto> getRecentLogins(UUID userId) {
        return repository.findTop10ByUserIdOrderByTimestampDesc(userId).stream()
                .map(entry -> LoginHistoryEntryDto.builder()
                        .ip(entry.getIp())
                        .userAgent(entry.getUserAgent())
                        .timestamp(entry.getTimestamp())
                        .build())
                .collect(Collectors.toList());
    }

    public void saveLogin(UUID userId, String ip, String userAgent) {
        LoginHistoryEntry entry = new LoginHistoryEntry();
        entry.setUserId(userId);
        entry.setIp(ip);
        entry.setUserAgent(userAgent);
        entry.setTimestamp(LocalDateTime.now());
        repository.save(entry);
    }

    public void saveLoginEvent(UserLoginEvent event) {
        LoginHistoryEntry entry = new LoginHistoryEntry();
        entry.setUserId(event.getUserId()); // ✅ уже UUID, не нужно преобразование
        entry.setIp(event.getIp());
        entry.setUserAgent(event.getUserAgent());
        entry.setTimestamp(event.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        repository.save(entry);
    }
}