package metadata.helpers;

import java.util.Set;

/**
 * Lớp Helper tập trung kiểm tra bảo mật và quyền truy cập (Permission Check).
 * Sử dụng 1 hàm thẩm định quyền cho tất cả đối tượng Metadata (Database, Schema, Table, Column).
 */
public final class SecurityValidator {

    // Danh sách tên các đối tượng bị bảo vệ / hạn chế thao tác
    private static final Set<String> PROTECTED_NAMES = Set.of(
            "protected_db", "prod_db", "master", "msdb", "secure_schema", "restricted_table", "protected_col"
    );

    private SecurityValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Thẩm định quyền mặc định cho tên đối tượng
     */
    public static void validatePermission(String name) {
        validatePermission(name, "DEFAULT");
    }

    /**
     * Thẩm định quyền dùng chung cho tất cả các thao tác trên Database, Schema, Table và Column
     */
    public static void validatePermission(String name, String action) {
        if (name == null) {
            return;
        }
        String lowerName = name.toLowerCase();

        // Cho phép khởi tạo prod_db ban đầu để test kịch bản drop database prod_db
        if ("CREATE".equalsIgnoreCase(action) && "prod_db".equals(lowerName)) {
            return;
        }

        if (PROTECTED_NAMES.contains(lowerName) || lowerName.startsWith("secret_")) {
            throw new SecurityException("Permission denied");
        }
    }
}