package az.kon.academy.catalog.command.service.bootstrap;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "az.kon.academy.catalog.command.service")
public class CatalogCommandApplication {
    static void main(String[] args) {
        SpringApplication.run(CatalogCommandApplication.class, args);
    }
}
