``` mermaid
classDiagram
    direction TD

%% =====================================================
%% SEMANTIC ANALYSIS
%% =====================================================

class SemanticAnalyzer{
    <<Visitor>>
    -NameResolver nameResolver
    -TypeChecker typeChecker
    -GroupByValidator groupByValidator
    -OrderByValidator orderByValidator
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
    -MetadataModule metadataModule
    -Set~String~ registeredAliases
    +resolve(AST ast)
    +resolveTable(ASTNode node) boolean
    +resolveColumn(ASTNode node) boolean
    +resolveAlias(ASTNode node) boolean
    +getRegisteredAliases() Set~String~
}

class TableResolver{
    +resolveTable(ASTNode node) boolean
}

class ColumnResolver{
    +resolveColumn(ASTNode node) boolean
}

class AliasResolver{
    +resolveAlias(ASTNode node) boolean
}

%% =====================================================
%% TYPE CHECKING
%% =====================================================

class TypeChecker{
    -MetadataModule metadataModule
    +validate(AST ast)
    +checkExpression(ASTNode node) boolean
    +getMetadataModule() MetadataModule
}

class ExpressionTypeChecker{
    +checkExpression(ASTNode node) boolean
}

class FunctionTypeChecker{
    +checkFunction(ASTNode node) boolean
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

class AST{
    -ASTNode root
}

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