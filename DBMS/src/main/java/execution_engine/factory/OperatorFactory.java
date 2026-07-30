package execution_engine.factory;

import execution_engine.abstracts.ExecutionPlanNode;
import query_processor.plan.PhysicalPlanNode;

public class OperatorFactory {

    private final ScanOperatorFactory scanOperatorFactory;
    private final JoinOperatorFactory joinOperatorFactory;
    private final AggregateOperatorFactory aggregateOperatorFactory;
    private final SortOperatorFactory sortOperatorFactory;

    public OperatorFactory() {
        this.scanOperatorFactory = new ScanOperatorFactory();
        this.joinOperatorFactory = new JoinOperatorFactory();
        this.aggregateOperatorFactory = new AggregateOperatorFactory();
        this.sortOperatorFactory = new SortOperatorFactory();
    }

    public ExecutionPlanNode createOperator(PhysicalPlanNode node) {
        if (node == null) {
            return scanOperatorFactory.create();
        }
        String type = node.getPhysicalOperatorType();
        if (type == null) {
            return scanOperatorFactory.create();
        }
        switch (type.toUpperCase()) {
            case "JOIN":
                return joinOperatorFactory.create();
            case "AGGREGATE":
                return aggregateOperatorFactory.create();
            case "SORT":
                return sortOperatorFactory.create();
            case "SCAN":
            default:
                return scanOperatorFactory.create();
        }
    }
}
