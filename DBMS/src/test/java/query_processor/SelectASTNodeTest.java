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

    @Test
    @DisplayName("TC-13. Build Select AST Node")
    void buildSelectASTNode_ShouldPopulateTableProjectionAndWhereClause_WhenParsingSelectStatement() {
        // Arrange
        String tableName = "employees";
        List<String> projections = Arrays.asList("id", "name", "salary");

        // Act
        SelectASTNode selectNode = new SelectASTNode(tableName, projections, mockWhereCondition);

        // Assert
        assertThat(selectNode.getNodeName()).isEqualTo("SelectASTNode");
        assertThat(selectNode.getTableName()).isEqualTo("employees");
        assertThat(selectNode.getProjectionFields()).containsExactly("id", "name", "salary");
        assertThat(selectNode.getWhereCondition()).isEqualTo(mockWhereCondition);
    }

    @Test
    @DisplayName("TC-14. Projection Fields")
    void projectionFields_ShouldMaintainListOfColumns() {
        // Arrange
        List<String> fields = Arrays.asList("column_a", "column_b");

        // Act
        SelectASTNode node = new SelectASTNode("my_table", fields, null);

        // Assert
        assertThat(node.getProjectionFields()).hasSize(2).contains("column_a", "column_b");
    }

    @Test
    @DisplayName("TC-15. WHERE Condition")
    void whereCondition_ShouldReturnAssignedCondition() {
        // Act
        SelectASTNode node = new SelectASTNode("orders", Arrays.asList("order_id"), mockWhereCondition);

        // Assert
        assertThat(node.getWhereCondition()).isSameAs(mockWhereCondition);
    }

    @Test
    @DisplayName("TC-16. Build Complete AST")
    void buildAST_ShouldConstructCompleteSyntaxTree_WhenParsingSelectStatement() {
        // Arrange - Construct individual AST nodes according to SD-12
        IdentifierASTNode leftOperand = new IdentifierASTNode("age");
        LiteralASTNode rightOperand = new LiteralASTNode("30", "INT");
        BinaryOpASTNode whereOp = new BinaryOpASTNode(">", leftOperand, rightOperand);
        SelectASTNode selectNode = new SelectASTNode("users", Arrays.asList("name", "age"), whereOp);

        // Act
        AST ast = new AST(selectNode);

        // Assert
        assertThat(ast.getRootASTNode()).isInstanceOf(SelectASTNode.class);
        SelectASTNode rootSelect = (SelectASTNode) ast.getRootASTNode();
        assertThat(rootSelect.getTableName()).isEqualTo("users");
        assertThat(rootSelect.getProjectionFields()).containsExactly("name", "age");

        assertThat(rootSelect.getWhereCondition()).isInstanceOf(BinaryOpASTNode.class);
        BinaryOpASTNode whereBinOp = (BinaryOpASTNode) rootSelect.getWhereCondition();
        assertThat(whereBinOp.getOperatorType()).isEqualTo(">");
        assertThat(((IdentifierASTNode) whereBinOp.getLeftNode()).getValue()).isEqualTo("age");
        assertThat(((LiteralASTNode) whereBinOp.getRightNode()).getValue()).isEqualTo("30");
    }

    @Test
    @DisplayName("TC-17. Default Constructor")
    void defaultConstructor_ShouldSetNodeName() {
        // Act
        SelectASTNode selectNode = new SelectASTNode();

        // Assert
        assertThat(selectNode.getNodeName()).isEqualTo("SelectASTNode");
        assertThat(selectNode.getTableName()).isNull();
        assertThat(selectNode.getProjectionFields()).isNull();
        assertThat(selectNode.getWhereCondition()).isNull();
    }
}
