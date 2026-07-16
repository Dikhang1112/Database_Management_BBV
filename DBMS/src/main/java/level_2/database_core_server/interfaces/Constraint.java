package database_core_server.interfaces;

import database_core_server.Field;

public interface Constraint extends DatabaseObject {
    boolean validate(Field field, Object newValue);
}
