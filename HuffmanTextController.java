package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;

public class HuffmanTextController {

    @FXML
    Button back;

    @FXML
    Button convert;

    @FXML
    TextArea inputText;

    @FXML
    TextArea outputText;

    @FXML
    Label error;

    String storeInputText;
    HuffmanEncoding huffmanEncoding;


    public void backOnAction(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/huffmanCompression.fxml"));

        try{
            Scene mainInterfaceScene = new Scene(loader.load());
            Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainInterfaceScene);
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void convertOnAction(){

        StringBuilder temp = new StringBuilder();

        if(inputText.getText().isEmpty()){
            error.setText("No Text Detected");
            return;
        }

        storeInputText = inputText.getText();
        huffmanEncoding = new HuffmanEncoding(storeInputText);
        huffmanEncoding.toFrequency();

        huffmanEncoding.makeMinHeap();

        huffmanEncoding.makeHuffmanTree();
        huffmanEncoding.getCodes();

        outputText.setText(huffmanEncoding.displayCodes().toString());

        huffmanEncoding.compressText();
        outputText.appendText(huffmanEncoding.displayCompressedText().toString());





    }








}
