package query_processor;

import java.util.List;
import metadata.facade.MetadataModule;
import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import query_processor.ast.SelectASTNode;
import query_processor.ast.TableASTNode;
import query_processor.ast.WhereASTNode;
import query_processor.facade.QueryProcessor;
import query_processor.interfaces.ASTVisitor;
import query_processor.interfaces.CompilerStage;
import query_processor.lexical.Lexer;
import query_processor.optimizer.QueryOptimizer;
import query_processor.optimizer.QueryRewriter;
import query_processor.parser.ASTBuilder;
import query_processor.parser.SQLParser;
import query_processor.plan.*;
import query_processor.semantic.*;

public class Main {

    public static void main(String[] args) {
        // Factory Method pattern
        System.out.println("--- 1. FACTORY METHOD PATTERN ---");
        LogicalOperatorFactory logicalOperatorFactory = new LogicalOperatorFactory();
        PhysicalOperatorFactory physicalOperatorFactory = new PhysicalOperatorFactory();
        System.out.println("Initialized LogicalOperatorFactory & PhysicalOperatorFactory successfully.\n");

        // Builder pattern
        System.out.println("--- 2. BUILDER PATTERN ---");
        AST ast = new AST();
        LogicalPlanBuilder logicalPlanBuilder = new LogicalPlanBuilder(logicalOperatorFactory);
        LogicalPlan logicalPlan = logicalPlanBuilder.build(ast);
        System.out.printf("Logical Plan built successfully. Root node: %s\n",
                logicalPlan.getRoot() != null ? logicalPlan.getRoot().getOperatorType() : "empty");

        PhysicalPlanBuilder physicalPlanBuilder = new PhysicalPlanBuilder(physicalOperatorFactory);
        PhysicalPlan physicalPlan = physicalPlanBuilder.build(logicalPlan);
        System.out.printf("Physical Plan built successfully. Root node: %s\n",
                physicalPlan.getRoot() != null ? physicalPlan.getRoot().getPhysicalOperatorType() : "empty");

        // Facade pattern
        System.out.println("--- 3. FACADE PATTERN ---");
        QueryProcessor queryProcessor = new QueryProcessor(new Lexer(), new SQLParser(), new ASTBuilder(), new SemanticAnalyzer(),
                new QueryRewriter(), new QueryOptimizer(), new PlanGenerator());
        String sqlText = "SELECT * FROM users WHERE age > 18";
        PhysicalPlan compliedPlan = queryProcessor.compile(sqlText);
        System.out.println("Facade compiled query execution completed successfully.");

        // Composite pattern
        System.out.println("--- 4. Composite PATTERN ---");
        TableASTNode tableASTNode = new TableASTNode("users");
        WhereASTNode whereASTNode = new WhereASTNode("age > 18");
        SelectASTNode astNode = new SelectASTNode(tableASTNode, whereASTNode);
        AST astTree = new AST(astNode);
        System.out.printf("Composite AST Hierarchy built with root: %s\n", astTree.getRoot());
        // Visitor pattern
        System.out.println("--- 5. Visitor PATTERN ---");
        ASTVisitor printVisitor = new ASTVisitor() {
            @Override
            public void visit(ASTNode node) {
                if (node instanceof SelectASTNode) {
                    System.out.println("-> [Visitor] Visiting SelectASTNode (Nút gốc SELECT)");
                } else if (node instanceof TableASTNode table) {
                    System.out.println("   -> [Visitor] Visiting TableASTNode: Table = " + table.getTableName());
                } else if (node instanceof WhereASTNode where) {
                    System.out.println("   -> [Visitor] Visiting WhereASTNode: Condition = " + where.getCondition());
                } else if (node != null) {
                    System.out.println("-> [Visitor] Visiting ASTNode: " + node.getClass().getSimpleName());
                }
            }
        };
        System.out.println("--- Demo 1: ASTVisitor (PrintVisitor) ---");
        astTree.getRoot().accept(printVisitor);
        // Chain of Responsibility pattern
        System.out.println("\n--- 6. CHAIN OF RESPONSIBILITY PATTERN ---");
        List<CompilerStage> compilerPipeline = List.of(
                new Lexer(),        // Stage 1: String -> TokenStream
                new SQLParser(),    // Stage 2: TokenStream -> ParseTree
                new ASTBuilder()    // Stage 3: ParseTree -> AST
        );

        String sampleSql = "SELECT * FROM users WHERE age > 18";
        Object inputData = sampleSql;
        System.out.println("Input SQL Text: " + sampleSql);

        for (CompilerStage stage : compilerPipeline) {
            System.out.printf("[Pipeline] Executing Stage: %-12s | Input Type: %s\n",
                    stage.getClass().getSimpleName(),
                    inputData != null ? inputData.getClass().getSimpleName() : "null");
            inputData = stage.process(inputData);
        }

        System.out.println("Chain of Responsibility traversal completed. Final output type: " +
                (inputData != null ? inputData.getClass().getSimpleName() : "null"));
    }
}
