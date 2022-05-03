package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindAccountPageController extends Controller{

    @FXML private Text resultText;
    @FXML private TextField idField;
    @FXML private TextField phoneFieldFirst;
    @FXML private Pane valuePane;
    @FXML private RadioButton accountIdRadioButton;
    @FXML private TextField nameField;
    @FXML private RadioButton nomalMemberRadioButton;
    @FXML private Text nameText;
    @FXML private TextField phoneFieldThird;
    @FXML private RadioButton accountPasswordRadioButton;
    @FXML private RadioButton businessMemberRadioButton;
    @FXML private TextField phoneFieldSecond;
    @FXML private ToggleGroup memberType;
    @FXML private Text idText;
    @FXML private ToggleGroup account;

    @FXML
    void moveLoginPage(MouseEvent event) throws IOException {
        movePage(event, "LoginPageUI.fxml");
    }

    @FXML
    void changeForm(MouseEvent event){
        if (accountPasswordRadioButton.isSelected())    {
            idField.setVisible(true);
            idText.setVisible(true);
        } else {
            idField.setVisible(false);
            idText.setVisible(false);
        }

        if (nomalMemberRadioButton.isSelected())
            nameText.setText("이름");
        else
            nameText.setText("업체명");
    }
    @FXML
    void onClickSearchCloseButton(MouseEvent event){
        valuePane.setVisible(false);
    }
    @FXML
    void onClickFindAccountButton(MouseEvent event) throws SQLException {
        String memberType = nomalMemberRadioButton.isSelected()?"everyhotel.회원":"everyhotel.숙박업체";
        String phoneNumber = phoneFieldFirst.getText() + "-" + phoneFieldSecond.getText() + "-" + phoneFieldThird.getText();
        String idType = nomalMemberRadioButton.isSelected()?"회원아이디":"업체아이디";
        String nameType = nomalMemberRadioButton.isSelected()?"이름":"업체명";

        ResultSet result = accountIdRadioButton.isSelected()?
                dbc.sendQuryGet("SELECT "+ idType +" FROM " + memberType + " WHERE "+nameType+"=\'" +nameField.getText() + "\'and 연락처=\'" + phoneNumber+"\';"):
                dbc.sendQuryGet("SELECT 비밀번호 FROM " + memberType + " WHERE "+nameType+"=\'" +nameField.getText() + "\'and 연락처=\'" + phoneNumber +"\'and "+idType+"=\'" + idField.getText()+"\';");
        if (!result.next()) {
            resultText.setText("잘못 입력되었습니다.");
        }else {
            resultText.setText(result.getString(1));
        }
        valuePane.setVisible(true);
    }

}
