package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class BusinessSearchBookingPageController extends Controller{

    @FXML private ScrollPane DateSearchPane;
    @FXML private ScrollPane roomSearchPane;

    @FXML
    void moveBusinessMainPage(MouseEvent event) throws IOException {
        movePage(event, "BusinessMainPageUI.fxml");
    }

}
