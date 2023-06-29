package sample.cafekiosk.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.CafeKiosk;
import sample.cafekiosk.unit.beverage.Latte;

@SpringBootApplication
public class CafekioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafekioskApplication.class, args);
    }
}
