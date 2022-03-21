package io.ic1101.icblog.filter;

import io.ic1101.icblog.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws AuthenticationException {
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain,
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();

        String accessToken = TokenUtils.createToken(user, httpServletRequest);
        String refreshToken = TokenUtils.createRefreshToken(user, httpServletRequest);

        TokenUtils.createTokenResponse(accessToken, refreshToken, httpServletResponse);
    }
}
