package ar.juarce.webapp.config;

import ar.juarce.webapp.auth.BasicAuthFilter;
import ar.juarce.webapp.auth.ForbiddenRequestHandler;
import ar.juarce.webapp.auth.UnauthorizedRequestHandler;
import ar.juarce.webapp.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    private final BasicAuthFilter basicAuthFilter;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, BasicAuthFilter basicAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.basicAuthFilter = basicAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//        return authenticationManagerBuilder.build();
//    }

//    @Bean
//    @Primary
//    protected AuthenticationManagerBuilder configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//        return auth;
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Set session management to stateless
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Set exception handlers
                .exceptionHandling(e -> e
                        // Set unauthorized requests exception handler
                        .authenticationEntryPoint(new UnauthorizedRequestHandler())
                        // Set forbidden requests exception handler
                        .accessDeniedHandler(new ForbiddenRequestHandler())
                )

                .headers(header ->
                        header.cacheControl(HeadersConfigurer.CacheControlConfig::disable))

                // Set permissions on endpoints
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").authenticated())

                .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
