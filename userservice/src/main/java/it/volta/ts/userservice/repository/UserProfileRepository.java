package it.volta.ts.userservice.repository;

import it.volta.ts.userservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByEmail(String email);

    Optional<UserProfile> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<UserProfile> findByNicknameIgnoreCase(String nickname);
    List<UserProfile> findAllByNicknameContainingIgnoreCase(String partialNickname);

}