package level_2.database_core_server;

public class NetworkPacket {
    private byte packetType;
    private byte packetStatus;
    private int payloadLength;
    private byte[] payloadData;

    public NetworkPacket(byte packetType, byte packetStatus, int payloadLength, byte[] payloadData) {
        this.packetType = packetType;
        this.packetStatus = packetStatus;
        this.payloadLength = payloadLength;
        this.payloadData = payloadData;
    }

    public byte[] getPayload() {
        return payloadData;
    }

    public byte getPacketType() {
        return packetType;
    }

    public byte getPacketStatus() {
        return packetStatus;
    }

    public int getPayloadLength() {
        return payloadLength;
    }
}
