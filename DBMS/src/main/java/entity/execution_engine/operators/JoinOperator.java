package entity.execution_engine.operators;

import entity.execution_engine.abstracts.BinaryOperator;
import entity.execution_engine.abstracts.ExecutionPlanNode;
import entity.execution_engine.helpers.JoinContext;
import entity.execution_engine.helpers.JoinPredicate;
import entity.execution_engine.domain.Tuple;
import entity.execution_engine.strategy.HashJoinStrategy;
import entity.execution_engine.strategy.JoinStrategy;

public class JoinOperator extends BinaryOperator {

    private JoinStrategy joinStrategy;
    private JoinPredicate joinPredicate;
    private JoinContext joinContext;

    public JoinOperator() {
        this(new HashJoinStrategy());
    }

    public JoinOperator(JoinStrategy joinStrategy) {
        this.joinStrategy = joinStrategy != null ? joinStrategy : new HashJoinStrategy();
        this.joinPredicate = new JoinPredicate();
        this.joinContext = new JoinContext();
    }

    public JoinOperator(ExecutionPlanNode left, ExecutionPlanNode right, JoinStrategy joinStrategy) {
        super(left, right);
        this.joinStrategy = joinStrategy != null ? joinStrategy : new HashJoinStrategy();
        this.joinPredicate = new JoinPredicate();
        this.joinContext = new JoinContext();
    }

    @Override
    public void open() {
        super.open();
        if (left != null) left.open();
        if (right != null) right.open();
    }

    @Override
    public Tuple next() {
        super.next();
        if (joinStrategy != null) {
            return joinStrategy.execute(left, right);
        }
        return null;
    }

    @Override
    public void close() {
        if (left != null) left.close();
        if (right != null) right.close();
        super.close();
    }

    public JoinStrategy getJoinStrategy() {
        return joinStrategy;
    }

    public void setJoinStrategy(JoinStrategy joinStrategy) {
        this.joinStrategy = joinStrategy;
    }

    public JoinPredicate getJoinPredicate() {
        return joinPredicate;
    }

    public JoinContext getJoinContext() {
        return joinContext;
    }
}
