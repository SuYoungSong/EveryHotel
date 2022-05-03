package com.example.bookedroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;

public class DetailsRoomPageController extends Controller{

    @FXML private Pane rightImageChange;
    @FXML private Text roomPrice;
    @FXML private Text companyId;
    @FXML private Pane imagePane;
    @FXML private Text roomAddress;
    @FXML private Text companyPhone;
    @FXML private Text companyName;
    @FXML private Pane leftImageChange;
    @FXML private Text roomName;

    @FXML
    void moveBookingPage(MouseEvent event) throws IOException {
        movePage(event, "BooknigPageUI.fxml");
    }

    @FXML
    void onClickBooking(ActionEvent event) {
        // 예약이 되는 코드를 작성할 예정
    }

}
