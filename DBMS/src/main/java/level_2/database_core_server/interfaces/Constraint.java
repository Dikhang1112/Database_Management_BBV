package level_2.database_core_server.interfaces;
import level_2.database_core_server.*;

public interface Constraint extends database_core_server.interfaces.DatabaseObject {
    boolean validate(database_core_server.Field field, Object newValue);
}
