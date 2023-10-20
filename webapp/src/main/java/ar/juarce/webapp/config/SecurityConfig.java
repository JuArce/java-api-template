package ar.juarce.webapp.config;

import ar.juarce.webapp.auth.*;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

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

    @Value("classpath:jwkSet.json")
    private Resource jkkSetFile;

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
    public AuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() throws IOException, ParseException {
        final DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        final JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet());
        final JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.ES256, jwkSource);
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        final JwtDecoder jwtDecoder = new NimbusJwtDecoder(jwtProcessor);
        final JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        jwtAuthenticationProvider.setJwtAuthenticationConverter(new JwtToUserDetailsConverter(userDetailsService));
        return jwtAuthenticationProvider;
    }

    @Bean
    public JWKSet jwkSet() throws IOException, ParseException {
        final String s = FileCopyUtils.copyToString(
                new InputStreamReader(jkkSetFile.getInputStream())
        );
        return JWKSet.parse(s);
    }

    @Bean
    public JwtEncoder jwtEncoder() throws IOException, ParseException {
        final JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet());
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationConfiguration = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationConfiguration
                .authenticationProvider(daoAuthenticationProvider())
                .authenticationProvider(jwtAuthenticationProvider())
                .build();
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter(HttpSecurity http) throws Exception {
        final AuthenticationManager authenticationManager = authenticationManager(http);
        final AuthenticationEntryPoint authenticationEntryPoint = authenticationEntryPoint();
        final JwtEncoder jwtEncoder = jwtEncoder();
        return new CustomBasicAuthenticationFilter(authenticationManager, authenticationEntryPoint, jwtEncoder);
    }

    @Bean
    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter(HttpSecurity http) throws Exception {
        final AuthenticationManager authenticationManager = authenticationManager(http);
        // TODO implement custom BearerTokenAuthenticationEntryPoint
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

                // Disable caching
                .headers(header ->
                        header.cacheControl(HeadersConfigurer.CacheControlConfig::disable))

                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // Set permissions on endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll())

                // Add JWT & Basic Authentication filters
                .addFilter(basicAuthenticationFilter(http))
                .addFilter(bearerTokenAuthenticationFilter(http))

                .build();
    }
}
