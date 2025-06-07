package it.volta.ts.userservice.repository;


import it.volta.ts.userservice.entity.LoginHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntry, UUID> {
    List<LoginHistoryEntry> findTop10ByUserIdOrderByTimestampDesc(UUID userId);
}
