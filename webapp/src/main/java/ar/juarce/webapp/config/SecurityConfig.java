package ar.juarce.webapp.config;

import ar.juarce.webapp.auth.CustomBasicAuthenticationFilter;
import ar.juarce.webapp.auth.ForbiddenRequestHandler;
import ar.juarce.webapp.auth.UnauthorizedRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.text.ParseException;

/**
 *  <a href="https://www.baeldung.com/spring-security-basic-authentication">spring-security-basic-authentication</a>
 * <p>
 *  <a href="https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter">spring-deprecated-websecurityconfigureradapter</a>
 * <p>
 *  <a href="https://www.baeldung.com/role-and-privilege-for-spring-security-registration">role-and-privilege-for-spring-security-registration</a>
 */
@Configuration
@EnableWebSecurity
@ComponentScan("ar.juarce.webapp.auth")
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;
    private final Converter<Jwt, UsernamePasswordAuthenticationToken> jwtToUserDetailsConverter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          JwtDecoder jwtDecoder,
                          JwtEncoder jwtEncoder,
                          Converter<Jwt, UsernamePasswordAuthenticationToken> jwtToUserDetailsConverter) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
        this.jwtToUserDetailsConverter = jwtToUserDetailsConverter;
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
    public AuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() throws IOException, ParseException {
        final JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(jwtToUserDetailsConverter);
        return jwtAuthenticationProvider;
    }

    /**
     * AuthenticationManager with two AuthenticationProviders:
     * - DaoAuthenticationProvider
     * - JwtAuthenticationProvider
     * <p>
     * <a href="https://www.baeldung.com/spring-security-multiple-auth-providers#1-java-configuration">https://www.baeldung.com/spring-security-multiple-auth-providers#1-java-configuration</a>
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        final AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(jwtAuthenticationProvider())
                .build();
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter(HttpSecurity http) throws Exception {
        final AuthenticationManager authenticationManager = authenticationManager(http);
        final AuthenticationEntryPoint authenticationEntryPoint = authenticationEntryPoint();
        return new CustomBasicAuthenticationFilter(authenticationManager, authenticationEntryPoint, jwtEncoder);
    }

    @Bean
    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter(HttpSecurity http) throws Exception {
        final AuthenticationManager authenticationManager = authenticationManager(http);
        // TODO implement custom BearerTokenAuthenticationEntryPoint or AuthenticationFailureHandler
        return new BearerTokenAuthenticationFilter(authenticationManager);
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

                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Set permissions on endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll())

                // Add Basic & JWT Authentication filters
                .addFilter(basicAuthenticationFilter(http))
                .addFilter(bearerTokenAuthenticationFilter(http))

                .build();
    }
}
