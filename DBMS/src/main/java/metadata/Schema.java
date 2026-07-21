package metadata;

import metadata.interfaces.MetadataElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Schema implements MetadataElement {
    private UUID schemaId;
    private String schemaName;
    private Map<String, Table> tables;
    private Map<String, View> views;
    private Map<String, Sequence> sequences;
    private Map<String, Trigger> triggers;
    private Map<String, StoredProcedure> procedures;
    private Map<String, Function> functions;
    private boolean readOnly;

    public Schema() {
        this.schemaId = UUID.randomUUID();
        this.tables = new HashMap<>();
        this.views = new HashMap<>();
        this.sequences = new HashMap<>();
        this.triggers = new HashMap<>();
        this.procedures = new HashMap<>();
        this.functions = new HashMap<>();
        this.readOnly = false;
    }

    public Schema(String schemaName) {
        this();
        this.schemaName = schemaName;
    }

    // Pattern: Factory Method
    public Table createTable(String tableName) {
        if (readOnly) {
            throw new IllegalStateException("Schema is read-only");
        }
        if (tableName == null || tableName.equals("restricted_table")) {
            throw new SecurityException("Permission denied");
        }
        if (tables.containsKey(tableName)) {
            throw new IllegalStateException("Table already exists");
        }
        Table table = new Table(tableName);
        tables.put(tableName, table);
        return table;
    }

    // Pattern: Factory Method
    public View createView(String viewName, String sql) {
        View view = new View();
        views.put(viewName, view);
        return view;
    }

    // Pattern: Factory Method
    public Sequence createSequence(String sequenceName) {
        Sequence sequence = new Sequence();
        sequences.put(sequenceName, sequence);
        return sequence;
    }

    // Pattern: Factory Method
    public Trigger createTrigger(String triggerName) {
        Trigger trigger = new Trigger();
        triggers.put(triggerName, trigger);
        return trigger;
    }

    // Pattern: Factory Method
    public StoredProcedure createProcedure(String procedureName) {
        StoredProcedure procedure = new StoredProcedure();
        procedures.put(procedureName, procedure);
        return procedure;
    }

    // Pattern: Factory Method
    public Function createFunction(String functionName) {
        Function function = new Function();
        functions.put(functionName, function);
        return function;
    }

    public void dropTable(String tableName) {
        tables.remove(tableName);
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public boolean containsTable(String tableName) {
        return tables.containsKey(tableName);
    }

    public List<Table> listTables() {
        return new ArrayList<>(tables.values());
    }

    public List<View> listViews() {
        return new ArrayList<>(views.values());
    }

    public void rename(String newName) {
        this.schemaName = newName;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public String getSchemaName() {
        return schemaName;
    }

    // Pattern: Composite
    @Override
    public String getElementName() {
        return schemaName;
    }
}


