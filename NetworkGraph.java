package org.example;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkGraph {

    public ConcurrentHashMap<String,Vertex> adjacencyList= new ConcurrentHashMap<>();

    public void addInteraction(String srcIp, String desIp, String srcMac, String desMac, String protocol){


        adjacencyList.putIfAbsent(srcIp, new Vertex(srcIp,srcMac));
        adjacencyList.putIfAbsent(desIp, new Vertex(desIp,desMac));

        Vertex srcVertex = adjacencyList.get(srcIp);

        boolean edgeExist = false;

        for(Edges edges: srcVertex.connection){

            if(desIp.equals(edges.getIp())){
                edgeExist = true;
                edges.setWeight(edges.getWeight()+1);
                break;
            }
        }

        if(!edgeExist){
            srcVertex.connection.add(new Edges(1,protocol, desIp));
        }


    }



}
