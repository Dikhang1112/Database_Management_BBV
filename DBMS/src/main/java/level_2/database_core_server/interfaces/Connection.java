package database_core_server.interfaces;

public interface Connection {
    void startListener(int port);
    void stopListener();
}
