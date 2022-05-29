package com.example.bookedroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class BookingPageController extends Controller implements Initializable {

    @FXML private CheckBox roomPeopleCheckBox;
    @FXML private DatePicker endDate;
    @FXML private ScrollPane bookingScrollPane;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private TextField peopleNum;
    @FXML private CheckBox roomTypeCheckBox;
    @FXML private DatePicker startDate;
    @FXML private Pane infoModalPane;
    @FXML private Text infoModalText;
    @FXML private Pane newFXMLPane;
    @FXML private BorderPane newFXMLCloseButton;
    @FXML
    void onClickCloseNewFXML(MouseEvent event) {
        newFXMLPane.setVisible(false);
        newFXMLCloseButton.setVisible(false);
        newFXMLPane.getChildren().removeAll();
    }
    @FXML
    void moveMemberMainPage(MouseEvent event) throws IOException {
        movePage(event, "MemberMainPageUI.fxml");
    }

    @FXML
    void onClickModalCloseButton(MouseEvent event) { infoModalPane.setVisible(false); }

    void setInfoModal(String msg){
        infoModalText.setText(msg);
        infoModalPane.setVisible(true);
    }

    @FXML
    void onClickBookingSearch(MouseEvent event) {
        java.util.Date nowDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 투숙일, 퇴실일 입력이 되지 않은경우 반려
        if (startDate.getValue() == null || endDate.getValue() == null) setInfoModal("기간을 입력해 주세요.");
        else {
            String sdate = sdf.format(Date.valueOf(startDate.getValue()));
            String edate = sdf.format(Date.valueOf(endDate.getValue()));
        // 오늘 이전의 날짜는 예약이 불가능하게 설정
         if (sdate.compareTo(sdf.format(nowDate)) < 0) {
            setInfoModal("투숙일은 오늘보다 빠를 수 없습니다.");
            // 퇴실일이 투숙일과 같거나 빠른경우 반려
        } else
            if (edate.compareTo(sdate) <= 0) {
            setInfoModal("투숙일은 퇴실일보다 같거나 늦을 수 없습니다.");
            // 인원수가 체크된 경우 비어있지 않은가 확인
        } else if (roomPeopleCheckBox.isSelected() && peopleNum.getText().equals("")) {
            setInfoModal("입력하지 않은 항목이 있습니다.");
        } else {
            // 검색하기
            String SQL = "SELECT 업체명, 호실, 숙소정원, 분류, 가격, 숙소번호, 주소 FROM everyhotel.숙소 natural join everyhotel.숙박업체 where 숙소.업체아이디=숙박업체.업체아이디";

            // 인원수가 설정된 경우
            if (roomPeopleCheckBox.isSelected()) {
                SQL += " and 숙소정원>=" + peopleNum.getText();
            }

            // 분류가 설정된 경우
            if (roomTypeCheckBox.isSelected()) {
                SQL += " and 분류=\'" + roomTypeComboBox.getValue() + "\'";
            }

            // 이미 예약된 방은 제외
            // 예약 테이블의 예약일, 퇴실일 기간 사이에  사용자가 입력한 투숙일과 퇴실일이 포함되어 있는 방을 제외하고 검색
            SQL += " and 숙소.숙소번호 not in ( select 예약.숙소번호 " +
                                                "from everyhotel.예약 " +
                                                "where ( \'" + sdate +"\' between 예약.예약일+1 and 퇴실일 ) or " +
                                                        "( \'"+ edate +"\' between 예약.예약일+1 and 퇴실일 ) or " +
                                                        "( 예약.예약일+1 between \'" + sdate +"\' and \' " + edate + "\') or " +
                                                        "( 예약.퇴실일 between \'" + sdate +"\' and \' " + edate + "\')  );";

            drowRooms(SQL, "조건에 맞는 숙소가 없습니다.");
        }
    }
    }
    void drowRooms(String SQL, String msg){
        bookingScrollPane.setContent(null);
        VBox vbItems = new VBox(5);
        List<Pane> roomList = new ArrayList<Pane>();
        ResultSet result = dbc.sendQuryGet(SQL);
        // 등록된 숙소가 있는 경우 추가해주기
        try {
            while (result.next()) {
                Pane roomPane = new Pane();
                roomPane.setPrefWidth(300);
                roomPane.setPrefHeight(120);
                roomPane.setPadding(new Insets(20));
                roomPane.setStyle("-fx-background-color: #EAAC41; -fx-background-radius: 5px;");

                String businessName = result.getString("업체명");
                String roomName = result.getString("호실");
                int roomMaxPeopleNum = result.getInt("숙소정원");
                String roomType = result.getString("분류");
                int roomPrice = result.getInt("가격");
                int roomNumber = result.getInt("숙소번호");
                String roomAddress = result.getString("주소");


                Label rn = new Label(businessName + " " + roomName);
                rn.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                rn.setLayoutX(10);
                rn.setLayoutY(15);

                Label rmp = new Label("최대인원: " + roomMaxPeopleNum+"");
                rmp.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                rmp.setLayoutX(12);
                rmp.setLayoutY(38);

                Label rt = new Label("분류: " + roomType);
                rt.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                rt.setLayoutX(12);
                rt.setLayoutY(56);

                Label rp = new Label("1박 가격: " + roomPrice+"원");
                rp.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                rp.setLayoutX(12);
                rp.setLayoutY(74);

                Label run = new Label(roomNumber+"");
                run.setVisible(false);

                Button roomViewButton = new Button("보기");
                roomViewButton.setOnMouseClicked(event -> { viewDetailRoomPage(event); });
                roomViewButton.setStyle("-fx-font-size: 20");
                roomViewButton.setLayoutX(210);
                roomViewButton.setLayoutY(15);
                roomViewButton.setPrefHeight(90);
                roomViewButton.setPrefWidth(80);

                Label ra = new Label("주소: " + roomAddress);
                ra.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                ra.setLayoutX(12);
                ra.setLayoutY(92);

                roomPane.getChildren().addAll(run,rn, rmp, rt, rp, roomViewButton, ra);
                vbItems.setMargin(roomPane, new Insets(12));
                roomList.add(roomPane);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        bookingScrollPane.setContent(vbItems);
        bookingScrollPane.setFitToWidth(true);

        if (roomList.size()>0) {
            for (Pane i : roomList) {
                vbItems.getChildren().add(i);
            }
        }else {
            Label temp = new Label(msg);
            temp.setStyle("-fx-font-size: 20;");
            temp.setLayoutX(25);
            temp.setLayoutY(180);
            Pane pTemp = new Pane();
            pTemp.getChildren().add(temp);
            vbItems.getChildren().add(pTemp);
            bookingScrollPane.setContent(vbItems);
            bookingScrollPane.setFitToWidth(true);
        }
    }

    private void viewDetailRoomPage(MouseEvent event) {
        newFXMLPane.getChildren().clear();
        Node nd = (Node) event.getSource();
        Label rn = (Label) nd.getParent().getChildrenUnmodifiable().get(0);
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("DetailsRoomPageUI.fxml"));
            Pane nf = loader.load();
            DetailsRoomPageController drpc = loader.<DetailsRoomPageController>getController();
            drpc.setting(Integer.valueOf(rn.getText()), startDate.getValue(), endDate.getValue());
            newFXMLPane.getChildren().add(nf);
            newFXMLPane.setVisible(true);
            newFXMLCloseButton.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String SQL = ("SELECT 업체명,호실,숙소정원,분류,가격,숙소번호, 주소 FROM everyhotel.숙소 natural join everyhotel.숙박업체 where 숙소.업체아이디=숙박업체.업체아이디;");
        ObservableList<String> boxList = FXCollections.observableArrayList("호텔","모텔","리조트","야영장","캠핑카","민박");
        roomTypeComboBox.setItems(boxList);
        roomTypeComboBox.setValue("호텔");
        drowRooms(SQL, "업체에서 등록한 숙소가 없습니다.");
    }
}