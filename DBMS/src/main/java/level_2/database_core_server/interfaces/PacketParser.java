package database_core_server.interfaces;

import database_core_server.NetworkPacket;

public interface PacketParser {
    NetworkPacket parseBuffer(byte[] rawBuffer);
    byte[] serializePacket(NetworkPacket packet);
}
