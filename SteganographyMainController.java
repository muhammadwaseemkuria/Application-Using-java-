package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class SteganographyMainController {

    @FXML
    Button back;

    @FXML
    ListView<?> hideMessage;

    @FXML
    ListView<?> extractMessage;

    @FXML
    public void backOnAction(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainInterface.fxml"));

        try{

            Scene mainInterfaceScene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainInterfaceScene);
            stage.show();


        }
        catch (Exception e){
            e.printStackTrace();
        }



    }


    public void hideMessageOnAction(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/hideMessage.fxml"));

        try{

            Scene steganographyMain = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(steganographyMain);
            stage.show();


        }
        catch (Exception e){
            e.printStackTrace();
        }



    }


    public void extractMessageOnAction(MouseEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/extractMessage.fxml"));

        try{

            Scene steganographyMain = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(steganographyMain);
            stage.show();


        }
        catch (Exception e){
            e.printStackTrace();
        }



    }



}
