package configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DBMS Engine API - Metadata Subsystem")
                        .version("1.0.0")
                        .description("REST API documentation simulating core Design Patterns in the Metadata Subsystem (Facade, Singleton, Command, Memento, Observer, Strategy).")
                        .contact(new Contact()
                                .name("DBMS Development Team")
                                .email("duykhanggt5@gmail.com")))

                // Server URL configuration
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Development Server")
                ))

                // Configure API group tags for Swagger UI
                .tags(List.of(
                        new Tag().name("1. Metadata Subsystem")
                                .description("Core REST APIs for FACADE & SINGLETON PATTERNS"),
                        new Tag().name("2. Catalog Management")
                                .description("REST APIs for managing the root CatalogManager and Databases"),
                        new Tag().name("3. Database Management")
                                .description("REST APIs for managing Schemas and Database Status"),
                        new Tag().name("4. Schema Management")
                                .description("REST APIs for managing Tables and Schema Read-Only properties"),
                        new Tag().name("5. Table Management")
                                .description("REST APIs for managing Tables, Snapshot Memento, and Event Listeners"),
                        new Tag().name("6. Column Management")
                                .description("REST APIs for managing Column attributes (Rename, Data Type)"),
                        new Tag().name("7. Constraint Management")
                                .description("REST APIs for managing Data Constraints (Enable / Disable)"),
                        new Tag().name("8. Index Management (Strategy Pattern)")
                                .description("REST APIs for Rebuilding Indexes using the Strategy Pattern")
                ));
    }
}
