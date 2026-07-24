``` mermaid
classDiagram
    direction TD

%% =====================================================
%% SEMANTIC ANALYSIS
%% =====================================================

class SemanticAnalyzer{
    <<Visitor>>
    +analyze(AST ast)
    +visit(ASTNode node)
}

class ASTVisitor{
    <<Interface>>
    +visit(ASTNode node)*
}

%% =====================================================
%% NAME RESOLUTION
%% =====================================================

class NameResolver{
    +resolve(AST ast)
}

class TableResolver{
    +resolveTable(ASTNode node)
}

class ColumnResolver{
    +resolveColumn(ASTNode node)
}

class AliasResolver{
    +resolveAlias(ASTNode node)
}

%% =====================================================
%% TYPE CHECKING
%% =====================================================

class TypeChecker{
    +validate(AST ast)
}

class ExpressionTypeChecker{
    +checkExpression(ASTNode node)
}

class FunctionTypeChecker{
    +checkFunction(ASTNode node)
}

%% =====================================================
%% SEMANTIC VALIDATION
%% =====================================================

class AggregateValidator{
    +validate(AST ast)
}

class GroupByValidator{
    +validate(AST ast)
}

class OrderByValidator{
    +validate(AST ast)
}

%% =====================================================
%% EXTERNAL MODULE
%% =====================================================

class MetadataModule{
    <<External Module>>
}

%% =====================================================
%% SHARED OBJECTS
%% =====================================================

class AST

class ASTNode

%% =====================================================
%% RELATIONSHIPS
%% =====================================================

ASTVisitor <|.. SemanticAnalyzer

SemanticAnalyzer *-- NameResolver
SemanticAnalyzer *-- TypeChecker

SemanticAnalyzer *-- AggregateValidator
SemanticAnalyzer *-- GroupByValidator
SemanticAnalyzer *-- OrderByValidator

NameResolver *-- TableResolver
NameResolver *-- ColumnResolver
NameResolver *-- AliasResolver

TypeChecker *-- ExpressionTypeChecker
TypeChecker *-- FunctionTypeChecker

SemanticAnalyzer --> AST

AST *-- ASTNode

TableResolver ..> MetadataModule
ColumnResolver ..> MetadataModule
AliasResolver ..> MetadataModule

ExpressionTypeChecker ..> MetadataModule
FunctionTypeChecker ..> MetadataModule
```