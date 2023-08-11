package ar.juarce.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
