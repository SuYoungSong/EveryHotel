package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MemberMainPageController extends Controller{

    @FXML
    void onClickLogoutButton(MouseEvent event) throws IOException {
        movePage(event, "MainPageUI.fxml");
    }
    @FXML
    void moveBookingPage(MouseEvent event) throws IOException {
        movePage(event, "BookingPageUI.fxml");
    }

    @FXML
    void moveFindBookingPage(MouseEvent event) throws IOException {
        movePage(event, "MemberSearchBookinPageUI.fxml");
    }


}
