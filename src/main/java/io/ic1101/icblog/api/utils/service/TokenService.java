package io.ic1101.icblog.api.utils.service;

import io.ic1101.icblog.database.entity.RefreshTokenEntity;
import io.ic1101.icblog.database.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenEntity saveToken(RefreshTokenEntity refreshTokenEntity) {
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    public RefreshTokenEntity getToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public int clearExpiredTokens() {
        int count = 0;

        for (RefreshTokenEntity refreshTokenEntity : refreshTokenRepository.findAll()) {
            if (refreshTokenEntity.getExpiresAt() > System.currentTimeMillis()) {
                continue;
            }

            refreshTokenRepository.delete(refreshTokenEntity);
            count++;
        }

        return count;
    }
}
