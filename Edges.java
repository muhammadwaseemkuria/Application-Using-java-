package org.example;

public class Edges {

    private int weight;
    private String protocol;
    private String Ip;

    public Edges(int weight, String protocol, String ip) {
        this.weight = weight;
        this.protocol = protocol;
        Ip = ip;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        this.Ip = ip;
    }
}
