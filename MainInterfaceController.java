package org.example;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.pcap4j.core.PcapNetworkInterface;

import java.io.IOException;
import java.util.List;

public class MainInterfaceController {

    @FXML
    private ListView<?> packetCapturer;

    @FXML
    private ListView<?> compression;

    @FXML
    private ListView<?> steganography;

    @FXML
    public void packetCapturerClick(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/packetCapturer.fxml"));


        try{
            Scene packetCapturerScene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(packetCapturerScene);


            stage.show();

        }
        catch (Exception e){
            System.out.println(e);
        }


    }

    public void compressionOnAction(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainCompresssion.fxml"));

        try{

            Scene mainCompressionScene = new Scene(loader.load());
            Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainCompressionScene);

            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }





    }

    public void steganographyOnAction(MouseEvent event){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/steganographyMain.fxml"));

        try{

            Scene steganographyMainScene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setScene(steganographyMainScene);
            stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }




}
