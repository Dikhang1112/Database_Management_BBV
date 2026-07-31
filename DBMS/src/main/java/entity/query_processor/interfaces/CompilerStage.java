package entity.query_processor.interfaces;

/**
 * Giao diện đại diện cho một công đoạn (stage) trong chuỗi biên dịch SQL.
 * Áp dụng Pattern Chain of Responsibility.
 */
public interface CompilerStage {

    /**
     * Xử lý dữ liệu đầu vào và chuyển sang giai đoạn tiếp theo của pipeline.
     *
     * @param input Đối tượng đầu vào của giai đoạn xử lý.
     * @return Kết quả sau khi được xử lý bởi giai đoạn này.
     */
    Object process(Object input);
}
