package ar.juarce.webapp.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/*
    Defining the PasswordEncoder as a Component
    instead of a Bean, allows us to use it in the
    UserServiceImpl class without having circular dependencies.
 */
@Component
public class PasswordEncoder extends BCryptPasswordEncoder {

}
