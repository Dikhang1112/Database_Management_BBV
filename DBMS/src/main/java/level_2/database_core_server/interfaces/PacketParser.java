package level_2.database_core_server.interfaces;

import level_2.database_core_server.NetworkPacket;

public interface PacketParser {
    NetworkPacket parseBuffer(byte[] rawBuffer);
    byte[] serializePacket(NetworkPacket packet);
}
