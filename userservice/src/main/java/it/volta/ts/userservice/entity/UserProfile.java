package it.volta.ts.userservice.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    private UUID id; // тот же ID, что и в auth-service (из sub JWT)

    @Column(nullable = false, unique = true)
    private String email; // не меняется, просто дублируется для удобства

    @Column(nullable = false, unique = true)
    private String nickname;

    private String bio;

    private String avatarUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    @Column(nullable = false)
    private boolean publicProfile = true;
}
