package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class LoginPageController extends Controller{

    @FXML private PasswordField loginPasswordField;
    @FXML private TextField loginIdField;
    @FXML private RadioButton nomalMemberRadioButton;
    @FXML private ToggleGroup memberType;
    @FXML private RadioButton BusinessMemberRadioButton;
    @FXML private Pane loginModal;
    @FXML private Text contentText;
    @FXML
    void moveMainPage(MouseEvent event) throws IOException {
        movePage(event, "MainPageUI.fxml");
    }

    @FXML
    void moveFineAccountPage(MouseEvent event) throws IOException {
        movePage(event, "FindAccountPageUI.fxml");
    }
    @FXML
    void onClickCloseModal(MouseEvent event) { loginModal.setVisible(false); }
    void changeModalText(String msg){ contentText.setText(msg); }

    @FXML
    void onClickLoginButton(MouseEvent event) throws IOException, SQLException {
        String memberType = (nomalMemberRadioButton.isSelected()?"everyhotel.회원":"everyhotel.숙박업체");
        String memberId = (nomalMemberRadioButton.isSelected()?"회원아이디":"업체아이디");

        // 모든 항목이 작성되었는지 확인
        if (!(loginIdField.getText().equals("") || loginPasswordField.getText().equals(""))) {
            ResultSet result = dbc.sendQuryGet("SELECT " + memberId + ", 비밀번호 FROM " + memberType + " WHERE " + memberId + "=\'" + loginIdField.getText() + "\'");
            // 로그인
            if (result.next()) {
                String id = result.getString(memberId);
                String pw = result.getString("비밀번호");

                // 아이디 비밀번호가 일치한다면
                if (id.equals(loginIdField.getText()) && pw.equals(loginPasswordField.getText())) {
                    String fxmlPath = nomalMemberRadioButton.isSelected() ? "MemberMainPageUI.fxml" : "BusinessMainPageUI.fxml";
                    LoginData ld = new LoginData(memberType, id);
                    movePage(event, fxmlPath);
                }
            }
        }
        // 로그인 실패
        changeModalText("로그인 실패");
        loginModal.setVisible(true);

    }

}