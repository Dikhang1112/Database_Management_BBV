package query_processor;

import java.util.HashMap;
import java.util.Map;

public class StatisticsManager {
    private final Map<String, Long> tableCardinalities = new HashMap<>();

    public StatisticsManager() {
        tableCardinalities.put("users", 1000L);
        tableCardinalities.put("orders", 5000L);
    }

    public long estimateCardinality(String tableName) {
        if (!tableCardinalities.containsKey(tableName)) {
            throw new IllegalArgumentException("Table not found: " + tableName);
        }
        return tableCardinalities.get(tableName);
    }

    public double estimateSelectivity(String column, String value) {
        if (column == null || value == null) {
            return 0.0;
        }
        return 0.1;
    }
}
