package ar.juarce.persistence.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application-test.properties")
@ComponentScan({"ar.juarce.persistence"})
@EntityScan("ar.juarce.models")
@EnableTransactionManagement
public class TestConfig {
}
