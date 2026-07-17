package level_2.database_core_server.interfaces;
import level_2.database_core_server.interfaces.DatabaseObject;
import level_2.database_core_server.Field;
public interface Constraint extends DatabaseObject {
    boolean validate(Field field, Object newValue);
}
