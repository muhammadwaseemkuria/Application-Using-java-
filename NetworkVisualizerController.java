package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NetworkVisualizerController {

    @FXML
    Button back;
    @FXML
    private Canvas canvas;

    NetworkGraph networkGraph;
    DrawGraph drawGraph;
    PacketSniffer packetSniffer;

//    public void initialize(){
//        this.networkGraph = packetSniffer.getNetworkGraph();
//        this.drawGraph = new DrawGraph(canvas,networkGraph);
//        drawGraph.start();
//    }

    public void getPacketSniffer(PacketSniffer packetSniffer){
        this.packetSniffer = packetSniffer;

        this.networkGraph = this.packetSniffer.getNetworkGraph();
        this.drawGraph = new DrawGraph(canvas,networkGraph);
        drawGraph.start();
    }


    public void backOnAction(ActionEvent event){
        drawGraph.stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PacketCaptureMain.fxml"));

        try{

            Scene mainNetworkInterfaceScene = new Scene(loader.load());
            Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(mainNetworkInterfaceScene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }


}
