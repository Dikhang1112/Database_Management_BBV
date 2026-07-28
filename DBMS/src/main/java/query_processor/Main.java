package query_processor;

import metadata.facade.MetadataModule;
import query_processor.abstracts.ASTNode;
import query_processor.ast.AST;
import query_processor.facade.QueryProcessor;
import query_processor.interfaces.ASTVisitor;
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
        ASTNode rootNode = new ASTNode() {
            @Override
            public void accept(ASTVisitor visitor) {
                System.out.println("ASTNode đã nhận Visitor: " + visitor.getClass().getSimpleName());
            }
        };

        //Pack ASTNode into AST
        AST astTree = new AST(rootNode);
        System.out.printf("Composite AST Hierarchy Root Node Type: %s\n",
                astTree.getRoot());

        // Visitor pattern
        System.out.println("--- 5. Visitor PATTERN ---");
        ASTVisitor visitor = new SemanticAnalyzer(
                new NameResolver(MetadataModule.getInstance()),
                new TypeChecker(MetadataModule.getInstance()),
                new GroupByValidator(),
                new OrderByValidator()
        );
        System.out.println("Sending ASTVisitor to visit and inspect AST nodes...");
        astTree.getRoot().accept(visitor);
        System.out.println("AST traversal & semantic validation completed.");
    }
}
