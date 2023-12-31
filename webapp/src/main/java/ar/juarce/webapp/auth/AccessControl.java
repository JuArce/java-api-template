package ar.juarce.webapp.auth;

import ar.juarce.interfaces.UserService;
import ar.juarce.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * <a href="https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#_migrating_expressions">migrating_expressions</a>
 * <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/util/AntPathMatcher.html">AntPathMatcher</a>
 */
@Component()
public class AccessControl {

    private final UserService userService;

    @Autowired
    public AccessControl(UserService userService) {
        this.userService = userService;
    }

    public UserDetails getAuthenticatedUserDetails(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            return null;
        }
        return (UserDetails) authentication.getPrincipal();
    }

    public User getAuthenticatedUser(Authentication authentication) {
        final UserDetails userDetails = getAuthenticatedUserDetails(authentication);
        if (userDetails == null) {
            return null;
        }
        return userService.findByUsername(userDetails.getUsername()).orElse(null);
    }

    public boolean isAuthenticatedUser(Authentication authentication, Long id) {
        final User user = getAuthenticatedUser(authentication);
        return user != null && user.getId().equals(id);
    }
}
