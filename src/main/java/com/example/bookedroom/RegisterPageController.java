package com.example.bookedroom;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class RegisterPageController extends Controller {

    @FXML private TextField registerMemberIdField;
    @FXML private PasswordField registerMemberPasswordField;
    @FXML private TextField registerMemberNameField;
    @FXML private TextField registerMemberPhoneFieldFirst;
    @FXML private TextField registerMemberPhoneFieldSecond;
    @FXML private TextField registerMemberPhoneFieldThird;
    @FXML private DatePicker registerMemberDateField;

    @FXML private TextField registerBusinessIdField;
    @FXML private PasswordField registerBusinessPasswordField;
    @FXML private TextField registerBusinessNameField;
    @FXML private TextField registerBusinessPhoneFieldFirst;
    @FXML private TextField registerBusinessPhoneFieldSecond;
    @FXML private TextField registerBusinessPhoneFieldThird;
    @FXML private TextField registerBusinessAddressField;
    @FXML private TextField registerBusinessCompanyIdFieldFirst;
    @FXML private TextField registerBusinessCompanyIdFieldSecond;
    @FXML private TextField registerBusinessCompanyIdFieldThird;
    @FXML private Button gotoMainButton;
    @FXML private Button registerButton;
    @FXML private Button modalCloseButton;
    @FXML private Pane businessMemberPane;
    @FXML private Pane nomalMemberPane;
    @FXML private RadioButton nomalMemberRadioButton;
    @FXML private RadioButton BusinessMemberRadioButton;
    @FXML private ToggleGroup memberType;
    @FXML private Text modalText;
    @FXML private Pane modalPane;

    @FXML
    void moveMainPage(MouseEvent event) throws IOException {
        movePage(event, "MainPageUI.fxml");
    }
    void modalActive(String msg){
        registerButton.setDisable(true);
        modalText.setText(msg);
        modalPane.setVisible(true);
    }
    boolean nullCheck(List<String> nc){
        return nc.stream().filter(txt -> txt.equals("")).findAny().equals(Optional.empty())?false:true;
    }
    boolean passwordCheck(PasswordField password){
        return ((password.getText()).length()>3);
    }
    String fieldMerge(List<String> rd){
        Iterator<String> iterator = rd.iterator();
        String temp = "";
        while(iterator.hasNext()){
            temp += "\'"+iterator.next()+"\',";
        }
        return temp.substring(0,temp.length()-1);
    }

    boolean overlabCheckId(String id) throws SQLException {
        String memberType = nomalMemberRadioButton.isSelected()?"회원아이디":"업체아이디";
        String tableName = nomalMemberRadioButton.isSelected()?"everyhotel.회원":"everyhotel.숙박업체";

        ResultSet result = dbc.sendQuryGet("SELECT "+memberType+" FROM "+tableName+" WHERE "+memberType+"=\'"+id+"\';");
        if (result.next())
            return true;  // 중복된 아이디가 있음
        return false; // 중복된 아이디가 없음
    }

    @FXML
    void onClickRegisterButton(MouseEvent event) throws SQLException {
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        LocalDate memberBirDate = registerMemberDateField.getValue();
        int nowYear = Integer.valueOf(format.format(time));

        List<String> registerData = new ArrayList<>();
        String phoneNumber;
        String businessNumber;
        String idText = nomalMemberRadioButton.isSelected()?registerMemberIdField.getText():registerBusinessIdField.getText();

        // 입력된 내용을 집합에 저장
        if (nomalMemberRadioButton.isSelected()) {
            phoneNumber = registerMemberPhoneFieldFirst.getText() + "-" + registerMemberPhoneFieldSecond.getText() + "-" + registerMemberPhoneFieldThird.getText();
            registerData = List.of(registerMemberIdField.getText(),
                    registerMemberPasswordField.getText(),
                    registerMemberNameField.getText(),
                    phoneNumber,
                    String.valueOf(registerMemberDateField.getValue())
            );
        } else {
            businessNumber = registerBusinessCompanyIdFieldFirst.getText() + "-" + registerBusinessCompanyIdFieldSecond.getText() + "-" + registerBusinessCompanyIdFieldThird.getText();
            phoneNumber = registerBusinessPhoneFieldFirst.getText() + "-" + registerBusinessPhoneFieldSecond.getText() + "-" + registerBusinessPhoneFieldThird.getText();
            registerData = List.of(registerBusinessIdField.getText(),
                    registerBusinessNameField.getText(),
                    phoneNumber,
                    businessNumber,
                    registerBusinessPasswordField.getText(),
                    registerBusinessAddressField.getText()
            );
        }
        System.out.println(registerData);

        // 작성하지 않은 항목이 있는지 확인하는 코드
        if (nullCheck(registerData)) {
            modalActive("작성하지 않은 항목이 있습니다.");
        // 비밀번호가 조건을 만족하는지 확인하는 코드
        }else if (!(nomalMemberRadioButton.isSelected() ? passwordCheck(registerMemberPasswordField) : passwordCheck(registerBusinessPasswordField))) {
            modalActive("비밀번호 조건이 알맞지 않습니다.");
        // 미성년자인지 확인
        }else if(nomalMemberRadioButton.isSelected() && !(nowYear - Integer.valueOf(memberBirDate.getYear()) > 18)){
            modalActive("미성년자는 가입할 수 없습니다.");
        // 아이디 중복체크
        }else if (overlabCheckId(idText)){
            modalActive("중복된 아이디가 있습니다.");
        } else {
            String memberType = nomalMemberRadioButton.isSelected() ? "회원" : "숙박업체";
            int temp = dbc.sendQuryPost("INSERT INTO everyhotel." + memberType + " VALUES(" + fieldMerge(registerData) + ")");
            if (temp == -1) {
                modalActive("양식을 맞추어 입력해주세요.");
            }
            modalCloseButton.setDisable(true);
            gotoMainButton.setVisible(true);
            modalActive("회원가입이 완료되었습니다.");
        }
    }

    @FXML
    void changeForm(MouseEvent event) {
        if (BusinessMemberRadioButton.isSelected()){
            nomalMemberPane.setVisible(false);
            businessMemberPane.setVisible(true);
        }else{
            businessMemberPane.setVisible(false);
            nomalMemberPane.setVisible(true);
        }
    }

    @FXML
    void closeModalButton(MouseEvent event) {
        registerButton.setDisable(false);
        modalPane.setVisible(false);
    }
}

