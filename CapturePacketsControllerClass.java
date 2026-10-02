package org.example;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.*;


public class CapturePacketsControllerClass implements PacketListener{

    @FXML
    private Button back;

    @FXML
    private Button stop;

    @FXML
    private Button start;

    @FXML
    private Label startLabel;

    @FXML
    private TableView<CapturedPacket> packetTableView;

    @FXML
    private TableColumn<CapturedPacket,Integer> no;
    @FXML
    private TableColumn<CapturedPacket,String> time;
    @FXML
    private TableColumn<CapturedPacket,String> srcIp;
    @FXML
    private TableColumn<CapturedPacket,String> desIp;

    @FXML
    private TableColumn<CapturedPacket,String> protocol;

    @FXML
    private TableColumn<CapturedPacket,String > info;

    @FXML
    private TextArea rawPacketTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField filterField;

    @FXML
    private Button map;


    private ObservableList<CapturedPacket> capturedPacketObservableList = FXCollections.observableArrayList();
    private int packetCount = 0;
    private PacketSniffer packetSniffer;

    int selectedInterface;

    public void initialize(){

        no.setCellValueFactory(new PropertyValueFactory<>("number"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        srcIp.setCellValueFactory(new PropertyValueFactory<>("source"));
        desIp.setCellValueFactory(new PropertyValueFactory<>("destination"));
        protocol.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        info.setCellValueFactory(new PropertyValueFactory<>("info"));
        packetTableView.setItems(capturedPacketObservableList);

        packetSniffer = new PacketSniffer();
        packetSniffer.setPacketListener(this);

        packetTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            if(newValue!=null){
                displayPacketDescription(newValue);
            }
        });


    }
    @FXML
    public void filterFieldOnAction(){
        if(filterField.getText() != null){
            packetSniffer.setFilter = filterField.getText();
        }

    }



    public void displayPacketDescription(CapturedPacket selectedCapturedPacket){
        descriptionTextArea.setText(selectedCapturedPacket.getDescription().toString());

        String rawPacket = bytesToHex(selectedCapturedPacket.getRawData());
        rawPacketTextArea.setText(rawPacket);

    }

    public String bytesToHex(byte[] rawData){
        StringBuilder hexString = new StringBuilder();
        String temp;
        String returnString ="";
        int counter = 0;
        for(int i = 0; i < rawData.length; i+=2){
            temp = String.format("%02X",rawData[i]);
            hexString.append(temp).append(" ");
            counter++;
            if((counter+1)%8 == 0){
                hexString.append("\n");
            }
        }

        returnString = hexString.toString();

        return returnString;

    }



    @FXML
    public void backOnAction(ActionEvent event){

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


    @Override
    public void packetInfo(String source, String destination, String protocol, int length, String info, byte[] rawData, StringBuilder description) {

        packetCount++;
        CapturedPacket newCapturedPacket = new CapturedPacket(packetCount,000000,source,destination,protocol,length,info,rawData, description);
        System.out.println("packet info");
        Platform.runLater(() ->{
            capturedPacketObservableList.add(newCapturedPacket);
        });



    }


    @FXML
    public void StartCapturing(){
        if(selectedInterface == -1){
            startLabel.setText("No Interface is Selected");
        }
        else{
            try{
                packetSniffer.capturePackets(selectedInterface);
            }
            catch (Exception e){
                e.printStackTrace();
            }



        }


    }

    public void receiveSelectedInterface(int selectedInterface){
        this.selectedInterface = selectedInterface;

    }

    @FXML
    public void stopButtonOnAction(){
        packetSniffer.stopCapturing();
        Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        saveConfirmation.setHeaderText("Would you like to save the session history?");
        saveConfirmation.setContentText("Click OK to choose a location.");

        if(saveConfirmation.showAndWait().get() == ButtonType.OK){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Save Location");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            // Use the stop button's window as the parent
            File file = fileChooser.showSaveDialog(null);
            saveFile(file);
        }


    }

    @FXML
    public void mapButtonOnAction(ActionEvent event){
        packetSniffer.stopCapturing();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NetworkVisualization.fxml"));

        try{
            Scene networkVisualizationScene = new Scene(loader.load());
            Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
            NetworkVisualizerController controller = loader.getController();
            controller.getPacketSniffer(packetSniffer);

            stage.setScene(networkVisualizationScene);
            stage.show();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public void saveFile(File file){
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
                writer.println("==================================================");
                writer.println("PACKET CAPTURE HISTORY - " + java.time.LocalDateTime.now());
                writer.println("==================================================\n");

                int count = 1;



                Node temp = packetSniffer.history.head;

                while (temp != null) { // Loop until the end of the list
                    CapturedPacket p = temp.capturedPacket; // Access the Packet object

                    writer.println("Packet #" + count);
                    writer.println("Protocol: " + p.getProtocol());
                    writer.println("Source: " + p.getSource() + " -> Destination: " + p.getDestination());
                    writer.println("Details:\n" + p.getDescription().toString()); // Use the StringBuilder

                    // Add the raw hex data for a "neat" look
                    writer.println("Raw Data (Hex):");
                    writer.println(formatToHex(p.getRawData()));

                    writer.println("--------------------------------------------------\n");

                    temp = temp.next; // Move to the next node
                    count++;
                }
                System.out.println("History saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private String formatToHex(byte[] data) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(String.format("%02X ", data[i]));
                if ((i + 1) % 16 == 0) sb.append("\n"); // New line every 16 bytes
            }
            return sb.toString();
        }


}
