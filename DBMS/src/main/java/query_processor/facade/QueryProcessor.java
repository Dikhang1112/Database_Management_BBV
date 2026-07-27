package query_processor.facade;

import query_processor.lexical.Lexer;
import query_processor.lexical.TokenStream;
import query_processor.parser.SQLParser;
import query_processor.parser.ParseTree;
import query_processor.parser.ASTBuilder;
import query_processor.ast.AST;
import query_processor.semantic.SemanticAnalyzer;
import query_processor.optimizer.QueryRewriter;
import query_processor.optimizer.QueryOptimizer;
import query_processor.plan.PlanGenerator;
import query_processor.plan.LogicalPlan;
import query_processor.plan.PhysicalPlan;

/**
 * Entry point Facade tập trung cho toàn bộ pipeline biên dịch SQL trong module Query Processor.
 * Pattern: Facade Pattern.
 */
public class QueryProcessor {

    private final Lexer lexer;
    private final SQLParser parser;
    private final ASTBuilder astBuilder;
    private final SemanticAnalyzer semanticAnalyzer;
    private final QueryRewriter queryRewriter;
    private final QueryOptimizer queryOptimizer;
    private final PlanGenerator planGenerator;

    /**
     * Khởi tạo QueryProcessor với đầy đủ các thành phần pipeline.
     */
    public QueryProcessor(Lexer lexer,
                          SQLParser parser,
                          ASTBuilder astBuilder,
                          SemanticAnalyzer semanticAnalyzer,
                          QueryRewriter queryRewriter,
                          QueryOptimizer queryOptimizer,
                          PlanGenerator planGenerator) {
        this.lexer = lexer;
        this.parser = parser;
        this.astBuilder = astBuilder;
        this.semanticAnalyzer = semanticAnalyzer;
        this.queryRewriter = queryRewriter;
        this.queryOptimizer = queryOptimizer;
        this.planGenerator = planGenerator;
        // TODO: Future DBMS logic implementation
    }

    /**
     * Biên dịch câu lệnh SQL chuỗi văn bản thành Kế hoạch Vật lý (PhysicalPlan).
     *
     * @param sqlText Chuỗi câu lệnh SQL.
     * @return PhysicalPlan sẵn sàng cho ExecutionEngine.
     */
    public PhysicalPlan compile(String sqlText) {
        // TODO: Future DBMS logic implementation
        return null;
    }
}
