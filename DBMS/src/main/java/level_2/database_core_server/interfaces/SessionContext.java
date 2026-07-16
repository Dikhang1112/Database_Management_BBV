package database_core_server.interfaces;

public interface SessionContext {
    int getSessionID();
    String getCurrentUser();
    void updateState(String newState);
    void bindTransaction(int txID);
}
