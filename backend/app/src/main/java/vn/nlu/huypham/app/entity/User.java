package vn.nlu.huypham.app.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode.Include;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Include
    UUID id;
    String email;
    String displayName;
    String username;
    String password;
    String avatar;
    @Builder.Default
    boolean isEnabled = true;
    @Builder.Default
    boolean isAccountNonLocked = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @Column(updatable = false)
    private Long createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now().getEpochSecond();
    }
}
