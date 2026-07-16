package level_2.execution_engine;

public class ExecutionPlan {
    private int planID;
    private String optimizedTreeJson;

    public ExecutionPlan(int planID, String optimizedTreeJson) {
        this.planID = planID;
        this.optimizedTreeJson = optimizedTreeJson;
    }

    public int getRootOperatorID() {
        return -1;
    }
}
