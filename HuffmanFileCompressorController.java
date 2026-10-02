package org.example;

import com.sun.glass.ui.CommonDialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HuffmanFileCompressorController {

    @FXML
    Button back;

    @FXML
    Button selectFile;

    @FXML
    Button start;

    @FXML
    Button saveLocation;

    @FXML
    TextField selectedSaveLocation;

    @FXML
    TextField displayFilePath;

    @FXML
    Label codeLabel;

    @FXML
    ToggleGroup option;

    @FXML
    RadioButton compress, decompress;

    private File selectedInput;
    private File selectedDir;

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
    @FXML
    public void selectFileOnAction(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        if (compress.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            selectedInput = fileChooser.showOpenDialog(stage);
        } else {
            // For decompression, we select the FOLDER created during compression
            DirectoryChooser dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Select the Compressed Output Folder");
            selectedInput = dirChooser.showDialog(stage);
        }

        if (selectedInput != null) {
            displayFilePath.setText(selectedInput.getAbsolutePath());
        }
    }

    @FXML
    public void saveLocationOnAction(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        selectedDir = dirChooser.showDialog(stage);
        if (selectedDir != null) {
            selectedSaveLocation.setText(selectedDir.getAbsolutePath());
        }
    }

    @FXML
    public void startOnAction(ActionEvent event) {
        if (selectedInput == null || selectedDir == null) {
            codeLabel.setText("Please select file and location!");
            return;
        }

        try {
            if (compress.isSelected()) {
                runCompression();
            } else {
                runDecompression();
            }
            codeLabel.setText("Task Completed Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            codeLabel.setText("Error occurred!");
        }
    }

    private void runCompression() throws IOException {
        File compressedFolder = new File(selectedDir, "Compressed_Output");
        if (!compressedFolder.exists()) compressedFolder.mkdir();

        String content = Files.readString(selectedInput.toPath());
        HuffmanEncoding engine = new HuffmanEncoding(content);
        engine.toFrequency();
        engine.makeMinHeap();
        engine.makeHuffmanTree();
        engine.getCodes();
        engine.compressText();

        // Save Bits
        try (FileOutputStream fos = new FileOutputStream(new File(compressedFolder, "data.bin"))) {
            fos.write(engine.getBitArray());
        }
        // Save Metadata (The Tree/Map)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(compressedFolder, "metadata.tree")))) {
            oos.writeObject(engine.getCharToCodeTable());
        }
    }

    private void runDecompression() throws IOException, ClassNotFoundException {
        // Load Metadata
        File treeFile = new File(selectedInput, "metadata.tree");
        File dataFile = new File(selectedInput, "data.bin");

        HashMap<Character, String> charToCode;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(treeFile))) {
            charToCode = (HashMap<Character, String>) ois.readObject();
        }

        // Reverse map for decoding: <Code, Character>
        HashMap<String, Character> codeToChar = new HashMap<>();
        for (Map.Entry<Character, String> entry : charToCode.entrySet()) {
            codeToChar.put(entry.getValue(), entry.getKey());
        }

        // Read Bytes and turn back to Bit String
        byte[] bytes = Files.readAllBytes(dataFile.toPath());
        StringBuilder bitString = new StringBuilder();
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                bitString.append((b >> i & 1));
            }
        }

        // Decode Bit String using the map
        StringBuilder decodedText = new StringBuilder();
        String tempCode = "";
        for (int i = 0; i < bitString.length(); i++) {
            tempCode += bitString.charAt(i);
            if (codeToChar.containsKey(tempCode)) {
                decodedText.append(codeToChar.get(tempCode));
                tempCode = "";
            }
        }

        Files.writeString(new File(selectedDir, "decompressed_result.txt").toPath(), decodedText.toString());
    }

}
