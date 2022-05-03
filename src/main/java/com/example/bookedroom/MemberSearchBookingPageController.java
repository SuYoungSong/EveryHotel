package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MemberSearchBookingPageController extends Controller{

    @FXML
    private ScrollPane scrollPane;

    @FXML
    void moveMainPage(MouseEvent event) throws IOException {
        movePage(event, "MemberMainPageUI.fxml");
    }

}
