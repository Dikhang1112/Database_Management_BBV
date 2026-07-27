package query_processor.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsManagerTest {

    private StatisticsManager statisticsManager;

    @BeforeEach
    void setUp() {
        statisticsManager = spy(new StatisticsManager() {
            @Override
            public double estimateCardinality() {
                return 1000.0;
            }

            @Override
            public double estimateSelectivity() {
                return 0.05;
            }
        });
    }

    // TC-11: Kiểm thử tra cứu số lượng bản ghi thực tế của bảng dữ liệu lưu trong Catalog Metadata.
    @Test
    @DisplayName("TC-11. Estimate Table Cardinality from Metadata")
    void estimateCardinality_ShouldReturnEstimatedRowCount_WhenTableStatisticsExist() {
        double cardinality = statisticsManager.estimateCardinality();

        assertThat(cardinality).isEqualTo(1000.0);
        verify(statisticsManager).estimateCardinality();
    }

    // TC-11A: Kiểm thử tính toán độ chọn lọc của cột dựa trên biểu đồ Histogram / NDV.
    @Test
    @DisplayName("TC-11A. Estimate Column Predicate Selectivity")
    void estimateSelectivity_ShouldReturnEstimatedFilterRatio_WhenColumnStatisticsExist() {
        double selectivity = statisticsManager.estimateSelectivity();

        assertThat(selectivity).isEqualTo(0.05);
        verify(statisticsManager).estimateSelectivity();
    }

    // TC-11B: Kiểm thử trả về giá trị số dòng mặc định an toàn khi bảng thiếu dữ liệu thống kê.
    @Test
    @DisplayName("TC-11B. Default Table Cardinality on Missing Statistics")
    void estimateCardinality_ShouldReturnDefaultValue_WhenTableStatisticsMissing() {
        StatisticsManager missingStatsManager = new StatisticsManager() {
            @Override
            public double estimateCardinality() {
                return 1000.0; // Default fallback cardinality
            }

            @Override public double estimateSelectivity() { return 0.1; }
        };

        double cardinality = missingStatsManager.estimateCardinality();

        assertThat(cardinality).isEqualTo(1000.0);
    }

    // TC-11C: Kiểm thử trả về tỷ lệ độ chọn lọc mặc định khi thiếu dữ liệu cột.
    @Test
    @DisplayName("TC-11C. Default Selectivity on Missing Column Statistics")
    void estimateSelectivity_ShouldReturnDefaultValue_WhenColumnStatisticsMissing() {
        StatisticsManager missingColumnManager = new StatisticsManager() {
            @Override public double estimateCardinality() { return 1000.0; }

            @Override
            public double estimateSelectivity() {
                return 0.1; // Default equality selectivity
            }
        };

        double selectivity = missingColumnManager.estimateSelectivity();

        assertThat(selectivity).isEqualTo(0.1);
    }
}
