package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatisticsManagerTest {

    private final StatisticsManager statsManager = new StatisticsManager();

    // Kiểm thử ước tính số lượng bản ghi (Cardinality) của bảng dữ liệu
    @Test
    @DisplayName("TC-11. Estimate Table Cardinality")
    void estimateCardinality_ShouldReturnTableRowCount() {
        long cardinality = statsManager.estimateCardinality("users");

        assertThat(cardinality).isGreaterThan(0);
    }

    // Kiểm thử tính toán độ chọn lọc (Selectivity) của biểu thức điều kiện
    @Test
    @DisplayName("TC-11A. Estimate Predicate Selectivity")
    void estimateSelectivity_ShouldComputeFractionalSelectivity_ForPredicate() {
        double selectivity = statsManager.estimateSelectivity("status", "ACTIVE");

        assertThat(selectivity).isBetween(0.0, 1.0);
    }

    // Kiểm thử tung ngoại lệ khi tra cứu thống kê của bảng không tồn tại
    @Test
    @DisplayName("TC-11B. Estimate Cardinality - Table Not Found Exception")
    void estimateCardinality_ShouldThrowException_WhenTableNotFound() {
        assertThatThrownBy(() -> statsManager.estimateCardinality("unknown_table"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Table not found");
    }
}
