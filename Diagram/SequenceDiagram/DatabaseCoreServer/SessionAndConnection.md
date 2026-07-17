```mermaid
sequenceDiagram
    autonumber
    
    %% --- FIXED COMPONENT DECLARATIONS (ONLY ONE ROW AT THE TOP) ---
    actor User as User
    participant GW as NetworkGateway
    participant PP as TdsPacketParser
    participant SM as SessionManager
    participant CTX as SessionContext

    %% --- PHASE 1: LOGIN (AUTHENTICATION & INITIALIZATION) ---
    Note over User, SM: PHASE 1: USER AUTHENTICATION & SESSION CREATION
    User->>GW: Connect & send credential bytes (TCP Stream)
    GW->>PP: parseBuffer(rawBuffer)
    activate PP
    PP-->>GW: Return NetworkPacket (packetType = 0x12 [Login])
    deactivate PP
    
    GW->>SM: createSession(connectionID, clientIP)
    activate SM
    SM->>CTX: Initialize state with security tokens
    SM-->>GW: Return SessionContext reference
    deactivate SM
    
    %% --- LOGIN ORDER: STATE FIRST, PACKET LAST ---
    GW->>CTX: 6. updateState("IDLE") FIRST (Establish ready state inside engine)
    GW-->>User: 7. Send Login Success Packet LAST (Open gate for user queries)

    %% --- PHASE 2: EXECUTE SQL STATEMENT (PARSING TO VALIDATION) ---
    Note over User, CTX: PHASE 2: SQL STATEMENT EXECUTION FLOW
    User->>GW: Send data packet ("INSERT INTO users...")
    GW->>CTX: updateState("RUNNING_QUERY")
    GW->>PP: parseBuffer(rawBuffer)
    activate PP
    PP-->>GW: Return NetworkPacket (packetType = 0x01 [SQL])
    deactivate PP
    
    Note over GW: NetworkGateway extracts raw SQL text<br/>and delegates to Query Processor & Execution Engine.<br/>
    
    %% --- QUERY ORDER: PACKET FIRST, STATE LAST ---
    GW-->>User: 13. Return ResultSet / RowCount bytes FIRST (Free up network buffer)
    GW->>CTX: 14. updateState("IDLE")
```
