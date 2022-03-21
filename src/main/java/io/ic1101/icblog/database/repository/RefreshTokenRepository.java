package io.ic1101.icblog.database.repository;

import io.ic1101.icblog.database.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    RefreshTokenEntity findByToken(String token);
}
