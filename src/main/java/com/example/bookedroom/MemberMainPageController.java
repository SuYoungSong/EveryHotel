package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemberMainPageController extends Controller {

    @FXML private Pane outMemberOut;

    LoginData ld = new LoginData();

    @FXML
    void onClickMemberOut(MouseEvent event) {
        outMemberOut.setVisible(true);
    }
    @FXML
    void onClickOutModalAccept(MouseEvent event) throws IOException {
        dbc.sendQuryPost("delete from everyhotel.회원 where everyhotel.회원.회원아이디 =\'" + ld.getId() + "\';");
        movePage(event, "MainPageUI.fxml");
    }
    @FXML
    void onClickOutModalCLose(MouseEvent event) {
        outMemberOut.setVisible(false);
    }

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
