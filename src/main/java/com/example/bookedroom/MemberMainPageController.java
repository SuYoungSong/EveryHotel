package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberMainPageController extends Controller {
    LoginData ld = new LoginData();

    @FXML
    void onClickLogoutButton(MouseEvent event) throws IOException {
        LoginData ld = new LoginData("","");
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
