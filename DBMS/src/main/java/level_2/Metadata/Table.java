package level_2.Metadata;

import java.util.List;

public class Table {
    public void addColumn(Column column) {
    }

    public void removeColumn(String columnName) {
    }

    public void addConstraint(Constraint constraint) {
    }

    public void removeConstraint(String constraintName) {
    }

    public void addIndex(Index index) {
    }

    public void removeIndex(String indexName) {
    }

    public Column getColumn(String columnName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Index getIndex(String indexName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Column> listColumns() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Index> listIndexes() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Constraint> listConstraints() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
