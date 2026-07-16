package database_core_server.interfaces;

public interface SessionManager {
    SessionContext createSession(int connectionID, String clientIP);
    void terminateSession(int sessionID);
    SessionContext getSession(int sessionID);
}
