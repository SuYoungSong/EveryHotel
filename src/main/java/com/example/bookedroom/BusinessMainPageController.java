package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class BusinessMainPageController extends Controller{

    @FXML
    void moveSearchBookingPage(MouseEvent event) throws IOException {
        movePage(event, "BusinessSearchBookingPageUI.fxml");
    }

    @FXML
    void moveControlBookingPage(MouseEvent event) throws IOException {
        movePage(event,"ControlBookingPageUI.fxml");
    }

    @FXML
    void onClickLogoutButton(MouseEvent event) throws IOException {
        movePage(event, "MemberSearchBookinPageUI.fxml");
    }

}
