package execution_engine.abstracts;

import execution_engine.enums.ExecutionState;
import execution_engine.domain.Tuple;

public abstract class ExecutionPlanNode {

    protected ExecutionState state = ExecutionState.CREATED;

    public void open() {
        this.state = ExecutionState.OPEN;
    }

    public Tuple next() {
        if (state == ExecutionState.OPEN) {
            this.state = ExecutionState.RUNNING;
        }
        return null;
    }

    public void close() {
        this.state = ExecutionState.CLOSED;
    }

    public ExecutionState getState() {
        return state;
    }

    public void setState(ExecutionState state) {
        this.state = state;
    }
}
