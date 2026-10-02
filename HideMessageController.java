package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HideMessageController {

    @FXML
    Button back;

    @FXML
    Button saveLocation;

    @FXML
    Button coverPhoto;

    @FXML
    TextField saveLocationField;

    @FXML
    TextField coverPhotoField;

    @FXML
    ImageView photoField;

    @FXML
    TextArea inputMessage;

    @FXML
    Label messageLabel;

    @FXML
    Button hide;

    @FXML
    TextField keyField;


    File selectedPhoto;
    File saveDir;

    ImageSteganography imageSteganography;


    @FXML
    public void backOnAction(ActionEvent event){
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

    @FXML
    public void coverPhotoOnAction(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Png Files", "*.png"));
        selectedPhoto = fileChooser.showOpenDialog(null);
        if(selectedPhoto != null){
            Image image = new Image(selectedPhoto.toURI().toString());
            photoField.setImage(image);
            coverPhotoField.setText(selectedPhoto.getAbsolutePath());
        }



    }

    @FXML
    public void saveLocationOnAction(ActionEvent event){
        DirectoryChooser dirChooser = new DirectoryChooser();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        saveDir = dirChooser.showDialog(stage);
        if (saveDir != null) {
            saveLocationField.setText(saveDir.getAbsolutePath());
        }

    }


    @FXML
    public void hideOnAction(){
        if(coverPhotoField.getText().isEmpty()){
            messageLabel.setText("No Cover Photo is Selected");
            return;
        }
        if(saveLocationField.getText().isEmpty()){
            messageLabel.setText("No Save Location is set");
            return;
        }
        if(inputMessage.getText().isEmpty()){
            messageLabel.setText("No Text is entered");
            return;
        }
        if(keyField.getText().isEmpty()){
            messageLabel.setText("No key is entered");
            return;
        }
        String key = keyField.getText();
        if(!key.chars().allMatch(Character::isDigit)){
            messageLabel.setText("Key should only contain digits");
            return;
        }
        int realKey = Integer.parseInt(key);
        imageSteganography = new ImageSteganography(selectedPhoto.getAbsolutePath(),saveDir.getAbsolutePath() ,inputMessage.getText(), realKey);

        imageSteganography.hideText();
        messageLabel.setText("Message Hidden Successfully!");

    }



}
