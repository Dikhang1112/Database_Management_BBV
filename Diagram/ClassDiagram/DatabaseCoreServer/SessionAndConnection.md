```mermaid
classDiagram
    direction TB
    %% --- CORE INTERFACES ---
    class Connection {
        <<interface>>
        +startListener(int port) void
        +stopListener() void
    }
    note for Connection "Manages raw TCP/IP socket listeners"

    class PacketParser {
        <<interface>>
        +parseBuffer(byte[] rawBuffer) NetworkPacket
        +serializePacket(NetworkPacket packet) byte[]
    }
    note for PacketParser "Translates network bytes to structured packets"

    class SessionManager {
        <<interface>>
        +createSession(int connectionID, String clientIP) SessionContext
        +terminateSession(int sessionID) void
        +getSession(int sessionID) SessionContext
    }
    note for SessionManager "Orchestrates global connection lifecycles"

    class SessionContext {
        <<interface>>
        +updateState(String newState) void
        +bindTransaction(int txID) void
    }
    note for SessionContext "Defines isolated operational scope for a client"

    %% --- CONCRETE IMPLEMENTATION CLASSES ---
    class TdsPacketParser {
        -int maxPacketSize
        -String protocolVersion
    }
    note for TdsPacketParser "Concrete TDS protocol decoder/encoder"

    class SessionContextStateManager {
        -int sessionID [PK]
        -int currentTransactionID
        -String currentUser
        -String sessionState
    }
    note for SessionContextStateManager "Stateful container tracking per-user variables"

    class NetworkGateway {
        -int serverPort
        -int currentActiveConnections
        -List~SessionContextStateManager~ activeSessions
        -TdsPacketParser packetParser
    }
    note for NetworkGateway "Facade controller handling core network I/O"

    class NetworkPacket {
        -byte packetType
        -byte packetStatus
        -int payloadLength  
        -byte[] payloadData
        +getPayload() byte[]
    }
    note for NetworkPacket "Transient frame holding header flags and raw SQL text"

    %% --- UML RELATIONSHIPS WITH EXPLANATIONS ---
    Connection <|.. NetworkGateway : Implements OS socket orchestration
    PacketParser <|.. TdsPacketParser : Implements binary stream parsing
    SessionManager <|.. NetworkGateway : Implements global session registry
    SessionContext <|.. SessionContextStateManager : Implements thread-safe state container

    NetworkGateway "1" *-- "1" TdsPacketParser : uses
    NetworkGateway "1" *-- "*" SessionContextStateManager : tracks
    TdsPacketParser ..> NetworkPacket : creates
    NetworkGateway ..> NetworkPacket : processes
```
