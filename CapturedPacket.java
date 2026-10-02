package org.example;

public class CapturedPacket {

    private int number;
    private double time;
    private String source;
    private String destination;
    private String protocol;
    private int length;
    private String info;



    private StringBuilder description = new StringBuilder();



    private byte[] rawData;


    //ipv4 constructor
    public CapturedPacket(int number, double time, String source, String destination, String protocol, int length, String info, byte[] rawData, StringBuilder description) {
        this.number = number;
        this.time = time;
        this.source = source;
        this.destination = destination;
        this.protocol = protocol;
        this.length = length;
        this.info = info;
        this.rawData = rawData;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public double getTime() {
        return time;
    }


    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }


    public String getProtocol() {
        return protocol;
    }


    public int getLength() {
        return length;
    }


    public String getInfo() {
        return info;
    }


    public byte[] getRawData() {
        return rawData;
    }

    public StringBuilder getDescription() {
        return description;
    }

}
