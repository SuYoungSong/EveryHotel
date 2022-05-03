package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class BookingPageController extends Controller{

    @FXML private DatePicker endDate;
    @FXML private ScrollPane bookingScrollPane;
    @FXML private TextField peopleNum;
    @FXML private DatePicker startDate;

    @FXML
    void moveMemberMainPage(MouseEvent event) throws IOException {
        movePage(event, "MemberMainPageUI.fxml");
    }

    @FXML
    void onClickBookingSearch(ActionEvent event) {
        System.out.println("onClickBookingSearch 구현되지 않은 기능");
        // 데이터베이스에서  투숙일-퇴식일에 인원수에 맞게 들어갈 수 있는 방을 검색해서 보여줄 예정
    }

}