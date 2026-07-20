package query_processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SelectASTNodeTest {

    @Mock
    private ASTNode mockWhereCondition;

    // Kiểm thử tạo SelectASTNode lưu thông tin bảng, trường dữ liệu và điều kiện WHERE
    @Test
    @DisplayName("TC-06. Build Select AST Node")
    void buildSelectASTNode_ShouldStoreTableFieldsAndWhereCondition() {
        List<String> fields = Arrays.asList("id", "total");
        SelectASTNode node = new SelectASTNode("orders", fields, mockWhereCondition);

        assertThat(node.getTableName()).isEqualTo("orders");
        assertThat(node.getProjectionFields()).containsExactly("id", "total");
        assertThat(node.getWhereCondition()).isEqualTo(mockWhereCondition);
        assertThat(node.getNodeName()).isEqualTo("SelectASTNode");
    }

    // Kiểm thử cho phép điều kiện WHERE bằng null khi không có mệnh đề WHERE
    @Test
    @DisplayName("TC-06A. Null WHERE Condition Allowed")
    void buildSelectASTNode_ShouldAllowNullWhereCondition_WhenNoWhereClause() {
        List<String> fields = Arrays.asList("name");
        SelectASTNode node = new SelectASTNode("users", fields, null);

        assertThat(node.getTableName()).isEqualTo("users");
        assertThat(node.getWhereCondition()).isNull();
    }

    // Kiểm thử lưu trữ nhiều trường chiếu trong danh sách projectionFields
    @Test
    @DisplayName("TC-06B. Multiple Projection Fields")
    void buildSelectASTNode_ShouldSupportMultipleProjectionFields() {
        List<String> fields = Arrays.asList("col1", "col2", "col3", "col4", "col5");
        SelectASTNode node = new SelectASTNode("data_table", fields, null);

        assertThat(node.getProjectionFields()).hasSize(5);
        assertThat(node.getProjectionFields()).containsAll(fields);
    }
}
