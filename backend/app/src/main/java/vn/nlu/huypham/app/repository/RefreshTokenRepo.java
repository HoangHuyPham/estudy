package vn.nlu.huypham.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.nlu.huypham.app.annotation.SafeModifying;
import vn.nlu.huypham.app.entity.RefreshToken;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByUserId(UUID userId);

    @SafeModifying
    @Query("""
        UPDATE RefreshToken rt
        SET rt.isUsed = true
        WHERE rt.id = :id 
        AND rt.expiredAt >= :now
        AND rt.isUsed = false
            """)
    int useRefreshTokenIfValid(@Param("id") UUID id, @Param("now") long nowSec);
}
