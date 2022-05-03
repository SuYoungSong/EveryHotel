package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MainPageController extends Controller{
    @FXML
    void moveMemberLogin(MouseEvent event) throws IOException {
        movePage(event, "LoginPageUI.fxml");
    }

    @FXML
    void moveRegisterAccount(MouseEvent event) throws IOException {
        movePage(event, "RegisterPageUI.fxml");
    }
}

