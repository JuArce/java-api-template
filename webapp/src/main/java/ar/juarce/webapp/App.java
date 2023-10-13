package ar.juarce.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@SpringBootApplication(scanBasePackages = {
        "ar.juarce.webapp",
        "ar.juarce.services",
        "ar.juarce.interfaces",
        "ar.juarce.models",
        "ar.juarce.persistence"
})
@EntityScan(basePackages = {
        "ar.juarce.models"
})
@EnableTransactionManagement
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
