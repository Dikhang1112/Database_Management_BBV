package metadata.helpers;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Lớp Helper tập trung kiểm tra tính hợp lệ về mặt định danh và cấu trúc cho các đối tượng Catalog.
 */
public final class CatalogValidator {

    // Biên dịch Regex Pattern duy nhất 1 lần trong JVM
    private static final Pattern VALID_IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    private CatalogValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Thẩm định ký tự hợp lệ và không rỗng của tên (Database, Schema, Table, Column, Index)
     */
    public static void validateIdentifier(String name, String entityType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Value is empty");
        }
        if (!VALID_IDENTIFIER_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(entityType + " name contains invalid characters");
        }
    }

    /**
     * Kiểm tra trùng lặp tên
     */
    public static void ensureUniqueName(String name, Collection<String> existingNames, String entityType) {
        if (existingNames.contains(name)) {
            throw new IllegalStateException(entityType + " already exists");
        }
    }

    /**
     * Kiểm tra sự tồn tại (Dùng khi tham chiếu cột/bảng)
     */
    public static void ensureExists(String name, Collection<String> existingNames, String entityType) {
        if (!existingNames.contains(name)) {
            throw new IllegalArgumentException(entityType + " not found");
        }
    }
}