package query_processor.external;

/**
 * Module giao tiếp với Storage Engine / Catalog Metadata của DBMS.
 * Cung cấp các phương thức kiểm tra sự tồn tại của Bảng, Cột và Hàm.
 */
public class MetadataModule {

    /**
     * Khởi tạo đối tượng MetadataModule.
     */
    public MetadataModule() {
        // TODO: Future DBMS logic implementation
    }

    /**
     * Kiểm tra sự tồn tại của bảng trong cơ sở dữ liệu.
     *
     * @param tableName Tên bảng cần kiểm tra.
     * @return true nếu bảng tồn tại (mock trả về true).
     */
    public boolean tableExists(String tableName) {
        // TODO: Future DBMS logic implementation
        return true;
    }

    /**
     * Kiểm tra sự tồn tại của cột thuộc một bảng chỉ định.
     *
     * @param tableName  Tên bảng chứa cột.
     * @param columnName Tên cột cần kiểm tra.
     * @return true nếu cột tồn tại (mock trả về true).
     */
    public boolean columnExists(String tableName, String columnName) {
        // TODO: Future DBMS logic implementation
        return true;
    }

    /**
     * Kiểm tra sự tồn tại của hàm SQL trong catalog.
     *
     * @param functionName Tên hàm cần kiểm tra.
     * @return true nếu hàm tồn tại (mock trả về true).
     */
    public boolean functionExists(String functionName) {
        // TODO: Future DBMS logic implementation
        return true;
    }
}
