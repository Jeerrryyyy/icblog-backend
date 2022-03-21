package io.ic1101.icblog.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
