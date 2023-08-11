package ar.juarce.webapp.config;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/")
public class JerseyConfiguration extends ResourceConfig {

    @PostConstruct
    public void init() {
        packages("ar.juarce.webapp.controllers");
        packages("ar.juarce.webapp.exceptionMappers");
    }
}
