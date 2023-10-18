package ar.juarce.webapp.config;

import ar.juarce.webapp.auth.CustomBasicAuthenticationFilter;
import ar.juarce.webapp.auth.ForbiddenRequestHandler;
import ar.juarce.webapp.auth.UnauthorizedRequestHandler;
import ar.juarce.webapp.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/*
https://www.baeldung.com/spring-security-basic-authentication
https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter
https://www.baeldung.com/role-and-privilege-for-spring-security-registration
 */
@Configuration
@EnableWebSecurity
@ComponentScan("ar.juarce.webapp.auth")
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedRequestHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new ForbiddenRequestHandler();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BasicAuthenticationFilter basicAuthorizationFilter(HttpSecurity http,
                                                              AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        final AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        return new CustomBasicAuthenticationFilter(authenticationManager, authenticationEntryPoint);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Set session management to stateless
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set exception handlers
                .exceptionHandling(e -> e
                        // Set unauthorized requests exception handler
                        .authenticationEntryPoint(authenticationEntryPoint())
                        // Set forbidden requests exception handler
                        .accessDeniedHandler(accessDeniedHandler())
                )

                // Disable caching
                .headers(header ->
                        header.cacheControl(HeadersConfigurer.CacheControlConfig::disable))

                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Set permissions on endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll())

                // Set AuthenticationProvider
                .authenticationProvider(authenticationProvider())

                // Add Basic Authorization filter
                .addFilter(basicAuthorizationFilter(http, authenticationEntryPoint()))

                .build();
    }
}
