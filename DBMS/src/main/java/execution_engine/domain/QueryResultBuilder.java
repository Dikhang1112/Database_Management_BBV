package execution_engine.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResultBuilder {

    private final List<String> columns = new ArrayList<>();
    private final List<Tuple> rows = new ArrayList<>();
    private final Map<String, Object> metadata = new HashMap<>();

    public QueryResultBuilder addColumn(String column) {
        if (column != null) {
            columns.add(column);
        }
        return this;
    }

    public QueryResultBuilder addRow(Tuple row) {
        if (row != null) {
            rows.add(row);
        }
        return this;
    }

    public QueryResultBuilder addMetadata(String key, Object value) {
        if (key != null) {
            this.metadata.put(key, value);
        }
        return this;
    }

    public QueryResult build() {
        return new QueryResult(new ArrayList<>(columns), new ArrayList<>(rows), new HashMap<>(metadata));
    }
}
