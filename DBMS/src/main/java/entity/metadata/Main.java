package entity.metadata;

import entity.metadata.abstracts.Constraint;
import entity.metadata.builders.ColumnBuilder;
import entity.metadata.commands.CreateTableCommand;
import entity.metadata.constraints.ConstraintFactory;
import entity.metadata.constraints.ConstraintValidationChain;
import entity.metadata.domain.CatalogManager;
import entity.metadata.domain.Column;
import entity.metadata.domain.Database;
import entity.metadata.domain.Index;
import entity.metadata.domain.Schema;
import entity.metadata.domain.Table;
import entity.metadata.domain.TableMemento;
import entity.metadata.enums.DataType;
import entity.metadata.enums.DatabaseStatus;
import entity.metadata.enums.IndexType;
import entity.metadata.facade.MetadataModule;
import entity.metadata.interfaces.DDLCommand;
import entity.metadata.interfaces.MetadataElement;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   DEMO ALL DESIGN PATTERNS IN METADATA MODULE   ");
        System.out.println("=================================================\n");

        // 1. SINGLETON PATTERN
        System.out.println("--- 1. SINGLETON PATTERN ---");
        MetadataModule metadataModule = MetadataModule.getInstance();
        CatalogManager catalogManager = metadataModule.getCatalogManager();
        System.out.printf("CatalogManager instance name: %s (Type: %s)\n\n",
                catalogManager.getElementName(), catalogManager.getElementType());

        // 2. FACTORY METHOD & COMPOSITE PATTERNS
        System.out.println("--- 2. FACTORY METHOD & COMPOSITE PATTERNS ---");
        Database db1 = catalogManager.createDatabase("sales_db");
        Schema schema1 = db1.createSchema("public");
        Table table1 = schema1.createTable("users");

        // Composite Pattern uniform hierarchy lookup
        MetadataElement[] elements = new MetadataElement[]{catalogManager, db1, schema1, table1};
        for (MetadataElement elem : elements) {
            System.out.printf("Composite Node -> Name: %-12s | Type: %s\n", elem.getElementName(), elem.getElementType());
        }
        System.out.println();

        // 3. FACADE PATTERN
        System.out.println("--- 3. FACADE PATTERN ---");
        Table queriedTable = metadataModule.getTable("sales_db", "public", "users");
        System.out.printf("Facade Lookup Result -> Found Table: %s\n\n", queriedTable != null ? queriedTable.getTableName() : "null");

        // 4. BUILDER PATTERN
        System.out.println("--- 4. BUILDER PATTERN ---");
        Column colId = new ColumnBuilder("user_id")
                .setType(DataType.BIGINT)
                .setNullable(false)
                .build();
        Column colName = new ColumnBuilder("username")
                .setType(DataType.VARCHAR)
                .setNullable(true)
                .setDefaultValue("N/A")
                .build();
        table1.addColumn(colId);
        table1.addColumn(colName);
        System.out.printf("Built Columns via Builder: %s (%s), %s (%s)\n\n",
                colId.getColumnName(), colId.getDataType(),
                colName.getColumnName(), colName.getDataType());

        // 5. PROTOTYPE PATTERN
        System.out.println("--- 5. PROTOTYPE PATTERN ---");
        Table clonedTable = table1.clone();
        System.out.printf("Original Table Name: %s | Columns count: %d\n", table1.getTableName(), table1.listColumns().size());
        System.out.printf("Cloned Table Name:   %s | Columns count: %d\n\n", clonedTable.getTableName(), clonedTable.listColumns().size());

        // 6. OBSERVER PATTERN
        System.out.println("--- 6. OBSERVER PATTERN ---");
        table1.registerListener((eventType, targetName) ->
                System.out.printf(" -> [OBSERVER NOTIFICATION] Event: %s | Target: %s\n", eventType, targetName));

        System.out.println("Adding new column to trigger Observer...");
        Column colAge = new ColumnBuilder("age_col").setType(DataType.INT).build();
        table1.addColumn(colAge); // Will trigger listener
        System.out.println();

        // 7. MEMENTO PATTERN
        System.out.println("--- 7. MEMENTO PATTERN ---");
        System.out.printf("Columns before snapshot: %d\n", table1.listColumns().size());
        TableMemento memento = table1.createMemento();

        System.out.println("Modifying table state (removing column 'username')...");
        table1.removeColumn("username");
        System.out.printf("Columns after removal: %d\n", table1.listColumns().size());

        System.out.println("Restoring table state from Memento...");
        table1.restore(memento);
        System.out.printf("Columns after restore: %d\n\n", table1.listColumns().size());

        // 8. COMMAND PATTERN
        System.out.println("--- 8. COMMAND PATTERN ---");
        DDLCommand createCmd = new CreateTableCommand("sales_db", "public", "orders");
        System.out.println("Executing DDL Command (Create Table 'orders')...");
        metadataModule.executeDDL(createCmd);
        System.out.printf("Table 'orders' exists: %b\n", schema1.containsTable("orders"));

        System.out.println("Undoing DDL Command (Drop Table 'orders')...");
        createCmd.undo();
        System.out.printf("Table 'orders' exists after undo: %b\n\n", schema1.containsTable("orders"));

        // 9. STATE PATTERN
        System.out.println("--- 9. STATE PATTERN ---");
        System.out.println("Setting Database Status to READ_ONLY...");
        db1.setStatus(DatabaseStatus.READ_ONLY);
        try {
            db1.createSchema("analytics");
        } catch (IllegalStateException e) {
            System.out.printf(" -> State Guard Blocked Operation: %s\n", e.getMessage());
        }
        db1.setStatus(DatabaseStatus.ONLINE);
        System.out.println("Restored Database Status to ONLINE.\n");

        // 10. TEMPLATE METHOD PATTERN
        System.out.println("--- 10. TEMPLATE METHOD PATTERN ---");
        Constraint pkConstraint = ConstraintFactory.createConstraint("PRIMARY_KEY", "pk_users");
        boolean isPkValid = pkConstraint.validate(); // Executes template algorithm
        System.out.printf("Constraint '%s' validation result: %b\n\n", pkConstraint.getConstraintName(), isPkValid);

        // 11. CHAIN OF RESPONSIBILITY PATTERN
        System.out.println("--- 11. CHAIN OF RESPONSIBILITY PATTERN ---");
        Constraint chkConstraint = ConstraintFactory.createConstraint("CHECK", "chk_age", "age >= 18");
        ConstraintValidationChain chain = new ConstraintValidationChain();
        chain.addConstraint(pkConstraint);
        chain.addConstraint(chkConstraint);

        boolean isChainValid = chain.validateAll();
        System.out.printf("Chain Validation Result (Fail-Fast Evaluation): %b\n\n", isChainValid);

        // 12. STRATEGY PATTERN
        System.out.println("--- 12. STRATEGY PATTERN ---");
        Index userIndex = new Index("idx_username", IndexType.BTREE);
        userIndex.setRebuildStrategy(index ->
                System.out.printf(" -> [STRATEGY EXECUTION] Rebuilding %s Index '%s' using BTree Strategy...\n",
                        index.getIndexType(), index.getIndexName()));

        userIndex.rebuild();
        System.out.println("\n=================================================");
        System.out.println("   DEMO COMPLETED SUCCESSFULLY!                  ");
        System.out.println("=================================================");
    }
}
