package ar.juarce.webapp.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Set;

@Configuration
public class JwtConfiguration {

    @Value("classpath:jwk_set.json")
    private Resource jwkSetFile;

    @Bean
    public JwtDecoder jwtDecoder() throws IOException, ParseException {
        return new NimbusJwtDecoder(jwtProcessor());
    }

    @Bean
    public JwtEncoder jwtEncoder() throws IOException, ParseException {
        return new NimbusJwtEncoder(jwkSource());
    }

    @Bean
    protected JWKSet jwkSet() throws IOException, ParseException {
        return JWKSet.parse(loadJwkSet());
    }

    private String loadJwkSet() throws IOException {
        return FileCopyUtils.copyToString(
                new InputStreamReader(jwkSetFile.getInputStream())
        );
    }

    @Bean
    protected JWTProcessor<SecurityContext> jwtProcessor() throws IOException, ParseException {
        final DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(jwsKeySelector());
        return jwtProcessor;
    }

    @Bean
    protected JWSKeySelector<SecurityContext> jwsKeySelector() throws IOException, ParseException {
        return new JWSVerificationKeySelector<>(jwsAlgorithms(), jwkSource());
    }

    /**
     * @return a set of supported JWS algorithms from the JWKSet
     */
    private Set<JWSAlgorithm> jwsAlgorithms() throws IOException, ParseException {
        final JWKSet jwkSet = jwkSet();
        return jwkSet.getKeys().stream()
                .map(jwk -> JWSAlgorithm.parse(jwk.getAlgorithm().getName()))
                .collect(java.util.stream.Collectors.toSet());
    }

    @Bean
    protected JWKSource<SecurityContext> jwkSource() throws IOException, ParseException {
        return new ImmutableJWKSet<>(jwkSet());
    }
}
