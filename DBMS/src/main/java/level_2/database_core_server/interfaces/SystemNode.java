package level_2.database_core_server.interfaces;

public interface SystemNode extends DatabaseObject {
    void addChild(DatabaseObject child);

    DatabaseObject getChild(int childID);
}
