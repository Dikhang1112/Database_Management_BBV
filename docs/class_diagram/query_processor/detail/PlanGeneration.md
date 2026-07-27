```mermaid
classDiagram
    direction TD

%% =====================================================
%% PLAN GENERATION
%% =====================================================

    class PlanGenerator{
+createLogicalPlan(AST ast) LogicalPlan
+createPhysicalPlan(LogicalPlan logicalPlan) PhysicalPlan
}

%% =====================================================
%% LOGICAL PLAN
%% =====================================================

class LogicalPlanBuilder{
<<Builder>>
+build(AST ast)
}

class LogicalOperatorFactory{
<<Factory Method>>
+createOperator(ASTNode node) LogicalPlanNode
}

class LogicalPlan

%% =====================================================
%% PHYSICAL PLAN
%% =====================================================

class PhysicalPlanBuilder{
<<Builder>>
+build(LogicalPlan logicalPlan) PhysicalPlan
}

class PhysicalOperatorFactory{
<<Factory Method>>
+createOperator(LogicalPlanNode node) PhysicalPlanNode
}

class PhysicalPlan

%% =====================================================
%% PLAN VALIDATION
%% =====================================================

class PlanValidator{
+validate(LogicalPlan logicalPlan)
}

class PlanNormalizer{
+normalize(LogicalPlan logicalPlan)
}

%% =====================================================
%% SHARED OBJECTS
%% =====================================================

class AST

class ASTNode

class LogicalPlanNode

class PhysicalPlanNode

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

PlanGenerator --> LogicalPlanBuilder
PlanGenerator --> PhysicalPlanBuilder
PlanGenerator --> PlanValidator
PlanGenerator --> PlanNormalizer

LogicalPlanBuilder --> LogicalOperatorFactory
LogicalPlanBuilder --> LogicalPlan

PhysicalPlanBuilder --> PhysicalOperatorFactory
PhysicalPlanBuilder --> PhysicalPlan

LogicalPlanBuilder --> AST
LogicalPlan --> LogicalPlanNode
PhysicalPlan --> PhysicalPlanNode

LogicalOperatorFactory --> LogicalPlanNode
PhysicalOperatorFactory --> PhysicalPlanNode

PlanValidator --> LogicalPlan

PlanNormalizer --> LogicalPlan
```