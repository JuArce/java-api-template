package ar.juarce.webapp.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtEncoder jwtEncoder;

    public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                           AuthenticationEntryPoint authenticationEntryPoint,
                                           JwtEncoder jwtEncoder) {
        super(authenticationManager, authenticationEntryPoint);
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
        final Jwt jwt = createJwt(authResult);
        addJwtToResponse(response, jwt);
    }

    private Jwt createJwt(Authentication authentication) {
        final JwsHeader header = JwsHeader.with(SignatureAlgorithm.ES256).build();
        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .build();

        final JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claims);
        return jwtEncoder.encode(parameters);
    }

    private void addJwtToResponse(HttpServletResponse response, Jwt jwt) {
        response.addHeader("Authorization", "Bearer " + jwt.getTokenValue());
    }
}
