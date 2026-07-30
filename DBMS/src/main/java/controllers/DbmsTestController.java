package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class DbmsTestController {

    // --- MODULE 1: METADATA ---
    @Tag(name = "1. Metadata Module")
    @Operation(summary = "Lấy danh sách các bảng trong Catalog", description = "API test danh mục Metadata")
    @GetMapping("/metadata/tables")
    public String getTables() {
        return "List of tables: [users, orders, products]";
    }

    // --- MODULE 2: STORAGE ENGINE ---
    @Tag(name = "2. Storage Engine Module")
    @Operation(summary = "Kiểm tra trạng thái Buffer Pool", description = "Lấy số lượng Page đang lưu trên RAM")
    @GetMapping("/storage/buffer-pool/status")
    public String getBufferPoolStatus() {
        return "Buffer Pool: 10/1000 pages in RAM";
    }

    // --- MODULE 3: QUERY PROCESSOR ---
    @Tag(name = "3. Query Processor Module")
    @Operation(summary = "Parse chuỗi SQL thành AST", description = "Kiểm tra cây cú pháp của câu lệnh SQL")
    @PostMapping("/processor/parse")
    public String parseSql(@RequestBody String sql) {
        return "Parsed AST for query: " + sql;
    }

    // --- MODULE 4: EXECUTION ENGINE ---
    @Tag(name = "4. Execution Engine Module")
    @Operation(summary = "Thực thi câu lệnh SQL", description = "Chạy kế hoạch truy vấn và trả về danh sách Tuples")
    @PostMapping("/execution/run")
    public String executeQuery(@RequestBody String sql) {
        return "Execution success for: " + sql;
    }
}
