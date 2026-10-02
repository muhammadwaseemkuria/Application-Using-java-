package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.pcap4j.core.PcapNetworkInterface;
import javafx.stage.Stage;

import java.util.List;

public class PacketCapturerController {


    @FXML
    private Button showInterfaces;
    @FXML
    private ComboBox<String> interfaceDropdown;
    private List<PcapNetworkInterface> interfaces;
    int selectedInterface;

    @FXML
    private Button back;

    @FXML
    private Button startCapturing;




    public void setInterfaces(List<PcapNetworkInterface> showInterfaces){
        interfaces = showInterfaces;
        String info;

        for (int i = 0; i < interfaces.size(); i++){
                PcapNetworkInterface temp = interfaces.get(i);
                info = i+" "+temp.getDescription();
                interfaceDropdown.getItems().add(info);
        }



    }


    @FXML
    public void showInterfaceOnAction(){

//        PacketSniffer.discoverInterfaces();
        List<PcapNetworkInterface> tempInterfaces = PacketSniffer.getInterfaces();
        setInterfaces(tempInterfaces);


    }

    @FXML
    public void backOnAction(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainInterface.fxml"));

        try{
            Scene mainInterfaceScene = new Scene(loader.load());
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setScene(mainInterfaceScene);
            stage.show();

        }catch(Exception e){
            System.out.println(e);

        }

    }

    public void startCapturingOnAction(ActionEvent event){

        selectedInterface = interfaceDropdown.getSelectionModel().getSelectedIndex();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PacketCaptureMain.fxml"));

        try {
            Scene CapturePacketsScene = new Scene(loader.load());

            CapturePacketsControllerClass nextController = loader.getController();
            nextController.receiveSelectedInterface(selectedInterface);


            Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();

            stage.setScene(CapturePacketsScene);
            stage.show();
        } catch (Exception e) {
            System.out.println(e);
        }



    }





}
