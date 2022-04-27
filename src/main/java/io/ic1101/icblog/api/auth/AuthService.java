package io.ic1101.icblog.api.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.ic1101.icblog.api.auth.exception.custom.TokenAlreadyInvalidatedException;
import io.ic1101.icblog.api.user.UserService;
import io.ic1101.icblog.api.utils.exception.custom.AuthHeaderMissingException;
import io.ic1101.icblog.api.utils.exception.custom.JwtParsingException;
import io.ic1101.icblog.api.utils.service.TokenService;
import io.ic1101.icblog.database.entity.RefreshTokenEntity;
import io.ic1101.icblog.database.entity.UserEntity;
import io.ic1101.icblog.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    public void handleTokenRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthHeaderMissingException, TokenAlreadyInvalidatedException, JwtParsingException {
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthHeaderMissingException("Please provide a valid auth header");
        }

        String refreshToken = authorizationHeader.substring("Bearer ".length());

        RefreshTokenEntity refreshTokenEntity = tokenService.getToken(refreshToken);

        if (refreshTokenEntity != null) {
            throw new TokenAlreadyInvalidatedException("The token was already invalidated!");
        }

        try {
            DecodedJWT decodedJWT = TokenUtils.decodeToken(refreshToken);
            String email = decodedJWT.getSubject();

            long expiresAt = decodedJWT.getExpiresAt().getTime();

            tokenService.saveToken(new RefreshTokenEntity(null, refreshToken, expiresAt));

            UserEntity userEntity = userService.getUser(email);

            String accessToken = TokenUtils.createToken(userEntity, httpServletRequest);
            String newRefreshToken = TokenUtils.createRefreshToken(userEntity, httpServletRequest);

            TokenUtils.createTokenResponse(accessToken, newRefreshToken, httpServletResponse);
        } catch (Exception ex) {
            throw new JwtParsingException("Please provide a valid jwt!");
        }
    }
}
