package execution_engine.domain;

import java.util.ArrayList;
import java.util.List;

public class Tuple {

    private final List<Object> values;

    public Tuple() {
        this.values = new ArrayList<>();
    }

    public Tuple(List<Object> values) {
        this.values = values != null ? values : new ArrayList<>();
    }

    public List<Object> getValues() {
        return values;
    }
}
