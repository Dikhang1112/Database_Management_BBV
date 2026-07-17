package level_2.database_core_server.interfaces;

public interface Connection {
    void startListener(int port);
    void stopListener();
}
