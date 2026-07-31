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
                        .title("Mini SQL Server Engine API - Metadata Subsystem")
                        .version("1.0.0")
                        .description("Tài liệu REST API kiểm thử và tương tác với Metadata Subsystem (MetadataModule Facade).")
                        .contact(new Contact()
                                .name("DBMS Development Team")
                                .email("duykhanggt5@gmail.com")))

                // Cấu hình Server URL
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Development Server")
                ))

                // Định hình nhóm API trên UI - Chỉ hiển thị Metadata Subsystem
                .tags(List.of(
                        new Tag().name("Metadata Subsystem")
                                .description("Quản lý thông tin Catalog, Database, Schema, Bảng (Tables), Cột (Columns), Constraint và Index thuộc MetadataModule Facade")
                ));
    }
}
