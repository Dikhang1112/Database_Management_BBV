package database_core_server.interfaces;

public interface SystemNode extends DatabaseObject {
    void addChild(DatabaseObject child);

    void addChild(database_core_server.interfaces.DatabaseObject child);

    DatabaseObject getChild(int childID);
}
