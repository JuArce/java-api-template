package ar.juarce.webapp.config;

import ar.juarce.webapp.controllers.HelloWorldController;
import ar.juarce.webapp.exceptionMappers.GenericExceptionMapper;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/")
public class JerseyConfiguration extends ResourceConfig {

    @PostConstruct
    public void init() {
        register(HelloWorldController.class);
        register(GenericExceptionMapper.class);
    }
}
