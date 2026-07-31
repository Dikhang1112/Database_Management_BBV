package entity.execution_engine.domain;

import entity.execution_engine.abstracts.ExecutionPlanNode;

public class ExecutionContext {

    private ExecutionPlanNode rootNode;

    public ExecutionContext() {
    }

    public ExecutionContext(ExecutionPlanNode rootNode) {
        this.rootNode = rootNode;
    }

    public ExecutionPlanNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(ExecutionPlanNode rootNode) {
        this.rootNode = rootNode;
    }
}
