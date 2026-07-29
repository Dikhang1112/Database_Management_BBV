package execution_engine.observer;

public interface ExecutionListener {

    void onExecutionStarted();

    void onTupleProcessed();

    void onExecutionFinished();
}
