package entity.query_processor;

import java.util.List;
import entity.metadata.facade.MetadataModule;
import org.w3c.dom.ls.LSOutput;
import entity.query_processor.abstracts.ASTNode;
import entity.query_processor.ast.AST;
import entity.query_processor.ast.SelectASTNode;
import entity.query_processor.ast.TableASTNode;
import entity.query_processor.ast.WhereASTNode;
import entity.query_processor.facade.QueryProcessor;
import entity.query_processor.interfaces.ASTVisitor;
import entity.query_processor.interfaces.CompilerStage;
import entity.query_processor.interfaces.OptimizationRule;
import entity.query_processor.lexical.Lexer;
import entity.query_processor.optimizer.CostBased;
import entity.query_processor.optimizer.QueryOptimizer;
import entity.query_processor.optimizer.QueryRewriter;
import entity.query_processor.optimizer.RuleBased;
import entity.query_processor.parser.ASTBuilder;
import entity.query_processor.parser.SQLParser;
import entity.query_processor.plan.*;
import entity.query_processor.semantic.*;

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
        // Chain of Responsibility pattern
        System.out.println("7. --- Strategy PATTERN ---");
        QueryOptimizer optimizer = new QueryOptimizer();
        LogicalPlan logicalPlan1 = new LogicalPlan();
        // Rule base strategy
        OptimizationRule ruleBasedStrategy = new RuleBased();
        optimizer.setOptimizationRule(ruleBasedStrategy);
        optimizer.getOptimizationRule().optimize(logicalPlan1);
        // Cost based strategy
        OptimizationRule costBasedStategy = new CostBased();
        optimizer.setOptimizationRule(costBasedStategy);
        optimizer.getOptimizationRule().optimize(logicalPlan1);
    }
}
