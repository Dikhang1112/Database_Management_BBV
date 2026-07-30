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
                        .title("Mini SQL Server Engine API")
                        .version("1.0.0")
                        .description("Tài liệu REST API kiểm thử và tương tác với các Core Modules của hệ thống DBMS.")
                        .contact(new Contact()
                                .name("DBMS Development Team")
                                .email("duykhanggt5@gmail.com")))

                // 2. Cấu hình Server URL
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ))

                // 3. Khởi tạo danh sách 4 Module chính để định hình nhóm API trên UI
                .tags(List.of(
                        new Tag().name("1. Metadata Module")
                                .description("Quản lý thông tin Catalog, Schema, Bảng (Tables) và Cột (Columns)"),
                        new Tag().name("2. Storage Engine Module")
                                .description("Thao tác thấp cấp với Đĩa vật lý (DiskManager), Trang (Pages) và Bộ nhớ đệm (BufferPool)"),
                        new Tag().name("3. Query Processor Module")
                                .description("Phân tích cú pháp SQL (Lexer/Parser) và Lập kế hoạch truy vấn (QueryPlanner/AST)"),
                        new Tag().name("4. Execution Engine Module")
                                .description("Thực thi kế hoạch truy vấn (Volcano Model: SeqScan, Filter, Project, Join Executors)")
                ));
    }
}
