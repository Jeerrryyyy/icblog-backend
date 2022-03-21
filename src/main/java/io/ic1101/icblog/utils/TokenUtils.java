package io.ic1101.icblog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ic1101.icblog.api.dto.response.ErrorDto;
import io.ic1101.icblog.api.dto.response.TokenDto;
import io.ic1101.icblog.database.entity.RoleEntity;
import io.ic1101.icblog.database.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenUtils {

    // TODO: Replace secret with real secret
    public static final Algorithm ALGORITHM = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));

    public static DecodedJWT decodeToken(String accessToken) {
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM).build();
        return jwtVerifier.verify(accessToken);
    }

    public static List<SimpleGrantedAuthority> createGrantedAuthorities(DecodedJWT decodedJWT) {
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return authorities;
    }

    public static String createToken(UserEntity userEntity, HttpServletRequest httpServletRequest) {
        return JWT.create()
                .withSubject(userEntity.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(httpServletRequest.getRequestURL().toString())
                .withClaim("roles", userEntity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList()))
                .sign(ALGORITHM);
    }

    public static String createToken(User user, HttpServletRequest httpServletRequest) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(httpServletRequest.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(ALGORITHM);
    }

    public static String createRefreshToken(UserEntity userEntity, HttpServletRequest httpServletRequest) {
        return JWT.create()
                .withSubject(userEntity.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(httpServletRequest.getRequestURL().toString())
                .sign(ALGORITHM);
    }

    public static String createRefreshToken(User user, HttpServletRequest httpServletRequest) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(httpServletRequest.getRequestURL().toString())
                .sign(ALGORITHM);
    }

    public static void handleError(Exception ex, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setHeader("error", ex.getMessage());
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON);

        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), new ErrorDto(ex.getMessage()));
    }

    public static void createTokenResponse(String accessToken, String refreshToken, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON);

        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), new TokenDto(accessToken, refreshToken));
    }
}
