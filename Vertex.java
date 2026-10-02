package org.example;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private String ip;
    private String mac;
    public double x,y;



    List<Edges> connection = new ArrayList<>();

    public Vertex(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
        this.x = Math.random() *900;
        this.y = Math.random() * 450;


    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
