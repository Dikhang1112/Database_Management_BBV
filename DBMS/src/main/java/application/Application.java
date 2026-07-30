package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "application",
        "configs",
        "controllers",
        "services",
        "dto",
        "repositories",
        "metadata",
        "storage_engine",
        "query_processor",
        "execution_engine"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
