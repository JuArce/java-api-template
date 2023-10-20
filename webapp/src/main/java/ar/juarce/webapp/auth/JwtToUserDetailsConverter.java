package ar.juarce.webapp.auth;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtToUserDetailsConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserDetailsChecker detailsChecker;

    public JwtToUserDetailsConverter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.detailsChecker = new AccountStatusUserDetailsChecker();
    }

    @Override
    public UsernamePasswordAuthenticationToken convert(@NonNull Jwt source) {
        UserDetails user = userDetailsService.loadUserByUsername(source.getSubject());
        detailsChecker.check(user);
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
}
