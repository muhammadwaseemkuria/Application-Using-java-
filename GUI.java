package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GUI extends Application{

        private Stage primaryStage;

        @Override
        public void start(Stage primaryStage) throws Exception {
            this.primaryStage = primaryStage;

            Parent root = FXMLLoader.load(getClass().getResource("/mainInterface.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }

        public Stage getPrimaryStage(){
            return this.primaryStage;
        }




}
