package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    private Stage stage;
    protected DataBaseConnection dbc;

    Controller() {
        dbc = new DataBaseConnection();
    }

    public void movePage(MouseEvent event, String fxmlPath) throws IOException{
        try {
            Node node = (Node) (event.getSource());
            stage = (Stage) (node.getScene().getWindow());
            Parent root = FXMLLoader.load(HelloApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.getMessage();
        }
    }
}
