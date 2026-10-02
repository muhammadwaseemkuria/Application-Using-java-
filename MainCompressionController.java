package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.lang.reflect.AccessFlag;
import java.util.List;

public class MainCompressionController {

    @FXML
    Button back;

    @FXML
    ListView<?> huffmanEncoding = new ListView<>();

    public void backOnAction(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainInterface.fxml"));

        try{
            Scene mainInterfaceScene = new Scene(loader.load());
            Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainInterfaceScene);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void huffmanEncodingOnAction(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huffmanCompression.fxml"));

        try{
            Scene huffmanEncryptionScene = new Scene(loader.load());
            Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(huffmanEncryptionScene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }






}
