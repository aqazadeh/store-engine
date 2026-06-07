package az.kon.academy.catalog.query.service.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "az.kon.academy.catalog.query.service")
public class CatalogQueryApplication {
    static void main(String[] args) {
        SpringApplication.run(CatalogQueryApplication.class, args);
    }
}
