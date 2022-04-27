package io.ic1101.icblog.api.auth;

import io.ic1101.icblog.api.auth.exception.custom.TokenAlreadyInvalidatedException;
import io.ic1101.icblog.api.utils.exception.custom.AuthHeaderMissingException;
import io.ic1101.icblog.api.utils.exception.custom.JwtParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthHeaderMissingException, TokenAlreadyInvalidatedException, JwtParsingException {
        authService.handleTokenRequest(httpServletRequest, httpServletResponse);
    }
}
