package org.example;

import java.time.Instant;

public class Node {
    CapturedPacket capturedPacket;
    long timestamp;
    Node next;

    public Node(CapturedPacket capturedPacket){
        this.capturedPacket = capturedPacket;
        next = null;
        timestamp = Instant.now().toEpochMilli();
    }

}

