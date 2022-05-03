package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ControlBookingPageController extends Controller{

    @FXML
    void moveMainPage(MouseEvent event) throws IOException {
        movePage(event, "BusinessMainPageUI.fxml");
    }

    @FXML
    void onClickAddRoom(ActionEvent event) {
        // 데이터 베이스에 새로운 숙소를 추가하는 코드를 작성할 예정
    }

}
