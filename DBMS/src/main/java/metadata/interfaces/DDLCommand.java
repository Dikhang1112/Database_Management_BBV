package metadata.interfaces;

/**
 * Interface cho Command Pattern trong metadata module.
 * Đóng gói các câu lệnh thay đổi DDL hỗ trợ thực thi execute() và hoàn tác undo().
 */
public interface DDLCommand {
    void execute();
    void undo();
}
