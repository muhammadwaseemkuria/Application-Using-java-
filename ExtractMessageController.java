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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ExtractMessageController {

    @FXML
    Button back;

    @FXML
    Button extract;

    @FXML
    Button photo;

    @FXML
    TextArea displayMessage;

    @FXML
    ImageView imageView;

    @FXML
    private TextField pathField;

    @FXML
    private TextField keyField;

    @FXML
    private File selectedPhoto;

    @FXML
    Label statusLabel;

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
    public void photoOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        selectedPhoto = fileChooser.showOpenDialog(null);

        if (selectedPhoto != null) {
            pathField.setText(selectedPhoto.getAbsolutePath());
            Image image = new Image(selectedPhoto.toURI().toString());
            imageView.setImage(image);
        }
    }

    @FXML
    public void extractOnAction(ActionEvent event) {
        if (selectedPhoto == null) {
            statusLabel.setText("Please select an image first.");
            return;
        }
        if (keyField.getText().isEmpty()) {
            statusLabel.setText("Please enter the key.");
            return;
        }

        try {
            int key = Integer.parseInt(keyField.getText());
            // Using a specific constructor for extraction
            ImageSteganography stego = new ImageSteganography(selectedPhoto.getAbsolutePath(), key);
            String result = stego.decodeImage();
            displayMessage.setText(result);
            statusLabel.setText("Extraction successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
