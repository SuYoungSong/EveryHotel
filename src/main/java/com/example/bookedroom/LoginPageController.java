package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class LoginPageController extends Controller{

    @FXML private PasswordField loginPasswordField;
    @FXML private TextField loginIdField;
    @FXML private RadioButton nomalMemberRadioButton;
    @FXML private ToggleGroup memberType;
    @FXML private RadioButton BusinessMemberRadioButton;

    @FXML
    void moveMainPage(MouseEvent event) throws IOException {
        movePage(event, "MainPageUI.fxml");
    }

    @FXML
    void moveFineAccountPage(MouseEvent event) throws IOException {
        movePage(event, "FindAccountPageUI.fxml");
    }

    @FXML
    void onClickLoginButton(MouseEvent event) throws IOException {
        // DB와 정보를 비교해서 로그인 진행 예정
        String fxmlPath = nomalMemberRadioButton.isSelected() ?  "MemberMainPageUI.fxml" : "BusinessMainPageUI.fxml";
        movePage(event, fxmlPath);
    }

}