package io.ic1101.icblog.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.ic1101.icblog.api.utils.exception.custom.AuthHeaderMissingException;
import io.ic1101.icblog.api.utils.exception.custom.JwtParsingException;
import io.ic1101.icblog.config.SecurityConfig;
import io.ic1101.icblog.utils.TokenUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    // TODO: Config?
    private final String apiKey = "abc";

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException, AuthHeaderMissingException, JwtParsingException {
        if (Arrays.asList(SecurityConfig.SKIP_URLS).contains(httpServletRequest.getServletPath())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthHeaderMissingException("Please provide a valid auth header");
        }

        String accessToken = authorizationHeader.substring("Bearer ".length());

        if (Arrays.asList(SecurityConfig.INTERNAL_API_URLS).contains(httpServletRequest.getServletPath())) {
            if (!accessToken.equals(apiKey)) {
                throw new AuthHeaderMissingException("Please provide a valid auth header");
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        try {
            DecodedJWT decodedJWT = TokenUtils.decodeToken(accessToken);
            String email = decodedJWT.getSubject();

            List<SimpleGrantedAuthority> authorities = TokenUtils.createGrantedAuthorities(decodedJWT);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            throw new JwtParsingException("Please provide a valid jwt!");
        }
    }
}
