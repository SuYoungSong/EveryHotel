package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class BusinessMainPageController extends Controller{
    @FXML private Pane outMemberOut;

    LoginData ld = new LoginData();

    @FXML
    void onClickMemberOut(MouseEvent event) {
        outMemberOut.setVisible(true);
    }
    @FXML
    void onClickOutModalAccept(MouseEvent event) throws IOException {
        dbc.sendQuryPost("delete from everyhotel.숙박업체 where everyhotel.숙박업체.업체아이디 =\'" + ld.getId() + "\';");
        movePage(event, "MainPageUI.fxml");
    }

    @FXML
    void onClickOutModalCLose(MouseEvent event) {
        outMemberOut.setVisible(false);
    }

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
        LoginData ld = new LoginData("","");
        movePage(event, "MainPageUI.fxml");
    }

}
