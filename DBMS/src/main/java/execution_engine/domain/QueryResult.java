package execution_engine.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResult {

    private List<String> columns = new ArrayList<>();
    private List<Tuple> rows = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();

    public QueryResult() {
    }

    public QueryResult(List<String> columns, List<Tuple> rows, Map<String, Object> metadata) {
        this.columns = columns != null ? columns : new ArrayList<>();
        this.rows = rows != null ? rows : new ArrayList<>();
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Tuple> getRows() {
        return rows;
    }

    public void setRows(List<Tuple> rows) {
        this.rows = rows;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
