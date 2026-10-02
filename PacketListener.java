package org.example;

public interface PacketListener {

    void packetInfo(String source,String destination, String protocol, int length, String info, byte[] rawData, StringBuilder description);

}
