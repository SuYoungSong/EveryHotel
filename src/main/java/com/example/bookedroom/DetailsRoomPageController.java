package com.example.bookedroom;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DetailsRoomPageController extends Controller{

    @FXML private Text bkModalDateText;
    @FXML private Text roomPrice;
    @FXML private DatePicker datePickerEndDate;
    @FXML private Pane imagePane;
    @FXML private Text bkModalPriceText;
    @FXML private ImageView imageView;
    @FXML private DatePicker datePickerStartDate;
    @FXML private ScrollPane reviewScrollPane;
    @FXML private Text roomName;
    @FXML private Text infoModalText;
    @FXML private Text bkModalAdressText;
    @FXML private Pane rightImageChange;
    @FXML private Text companyId;
    @FXML private Text maxPeopleNumText;
    @FXML private Text roomAddress;
    @FXML private Text companyPhone;
    @FXML private Pane bkPane;
    @FXML private Pane dateModalPane;
    @FXML private Pane leftImageChange;
    @FXML private Pane infoModal;
    @FXML private Pane successBkPane;

    private String businessName;
    private String roomDetailName;
    private int roomDetailPrice;
    private String businessAdress;
    private int roomDetailMaxPeople;
    private String businessPhone;
    private String businessNumber;

    private int roomNumber;
    private LocalDate sDate;
    private LocalDate eDate;
    private int nowImageNum = 0;
    ArrayList<String> imgUrl = new ArrayList<>();

    public void setting(int rn, LocalDate sd, LocalDate ed){
        this.roomNumber = rn;
        this.sDate = sd;
        this.eDate = ed;

        pageSetting();
        reviewSetting();
        imageSetting();
    }

    void pageSetting(){
        // 페이지 초기 설정
        ResultSet result = dbc.sendQuryGet("select 업체명, 호실, 가격, 주소, 숙소정원, 연락처, 사업자번호 from everyhotel.숙소 natural join everyhotel.숙박업체 where 숙소번호="+roomNumber+";");
        try {
            result.next();
            businessName = result.getString("업체명");
            roomDetailName = result.getString("호실");
            roomDetailPrice = result.getInt("가격");
            businessAdress = result.getString("주소");
            roomDetailMaxPeople = result.getInt("숙소정원");
            businessPhone = result.getString("연락처");
            businessNumber = result.getString("사업자번호");

            roomName.setText(businessName + " " + roomDetailName);
            roomAddress.setText(businessAdress);
            roomPrice.setText("1박 " + roomDetailPrice + "원");
            maxPeopleNumText.setText(roomDetailMaxPeople + "인");
            companyPhone.setText(businessPhone);
            companyId.setText(businessNumber);

        } catch (SQLException e) {
            System.out.println("숙소 정보 로딩 실패");
            System.out.println(e.getMessage());
        }
    }

    void reviewSetting(){
        // 리뷰 초기 설정
        VBox vbItems = new VBox(5);
        List<Pane> roomList = new ArrayList<Pane>();
        ResultSet result = dbc.sendQuryGet("select 회원아이디, 리뷰내용, 평점 from everyhotel.리뷰 natural join everyhotel.예약 where 리뷰.예약번호=예약.예약번호 and 숙소번호="+ roomNumber+";");

        try {
            while (result.next()) {
                String st = "";
                Pane roomPane = new Pane();
                roomPane.setPrefWidth(300);
                roomPane.setPrefHeight(68);
                roomPane.setPadding(new Insets(20));
                roomPane.setStyle("-fx-background-color: #C670E7; -fx-background-radius: 5px;");

                String memberId = result.getString("회원아이디");
                String reviewContent = result.getString("리뷰내용");
                int star = result.getInt("평점");

                Label mi = new Label("작성자: "+memberId);
                mi.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                mi.setLayoutX(10);
                mi.setLayoutY(15);

                Label rc = new Label(reviewContent);
                rc.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                rc.setLayoutX(10);
                rc.setLayoutY(33);

                for( int i = 0 ; i<star ; i++)
                    st += "★";

                Label score = new Label(st);
                score.setStyle("-fx-text-fill: yellow; -fx-font-size: 15;");
                score.setLayoutX(200);
                score.setPrefWidth(100);
                score.setAlignment(Pos.CENTER_RIGHT);
                score.setLayoutY(15);

                roomPane.getChildren().addAll(mi, rc, score);
                vbItems.setMargin(roomPane, new Insets(12));
                roomList.add(roomPane);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        reviewScrollPane.setContent(vbItems);
        reviewScrollPane.setFitToWidth(true);

        if (roomList.size()>0) {
            for (Pane i : roomList) {
                vbItems.getChildren().add(i);
            }
        }else {
            Label temp = new Label("작성된 리뷰가 없습니다.");
            temp.setStyle("-fx-font-size: 17;");
            temp.setLayoutX(75);
            temp.setLayoutY(60);
            Pane pTemp = new Pane();
            pTemp.getChildren().add(temp);
            vbItems.getChildren().add(pTemp);
            reviewScrollPane.setContent(vbItems);
            reviewScrollPane.setFitToWidth(true);
        }
    }

    void imageSetting(){
        ResultSet result = dbc.sendQuryGet("select 이미지주소 from everyhotel.이미지 where 숙소번호="+ roomNumber +";");
        try {
            while (result.next()){
                imgUrl.add(result.getString("이미지주소"));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if (imgUrl.size()<=0){
            System.out.println(HelloApplication.class.getResourceAsStream("no_image.png"));
            imageView.setImage(new Image(HelloApplication.class.getResourceAsStream("no_image.png")));
            centerImage();
            rightImageChange.setVisible(false);
            leftImageChange.setVisible(false);
        }else{
            leftImageChange.setVisible(false);
            imageView.setImage(new Image(imgUrl.get(nowImageNum)));
            centerImage();

            if (imgUrl.size() ==1) rightImageChange.setVisible(false);
        }
    }

    public void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }

    void setInfoModal(String msg){
        infoModalText.setText(msg);
        infoModal.setVisible(true);
    }



    @FXML
    void onClickSucessModalClose(MouseEvent event) { successBkPane.setVisible(false); }
    @FXML
    void onClickMoveBKSerPage(MouseEvent event) throws IOException { movePage(event, "MemberSearchBookinPageUI.fxml"); }
    @FXML
    void onClickSelDateCalcelButton(MouseEvent event) { dateModalPane.setVisible(false); }
    @FXML
    void onClickCloseInfoModal(MouseEvent event) { infoModal.setVisible(false); }
    @FXML
    void onClickSelDateModal(MouseEvent event) {
        // 필터로 이용일을 지정했었던 경우 받아오기
        if (sDate != null) datePickerStartDate.setValue(sDate);
        if (eDate != null) datePickerEndDate.setValue(eDate);
        dateModalPane.setVisible(true);
    }
    @FXML
    void moveBookingPage(MouseEvent event) throws IOException { movePage(event, "BooknigPageUI.fxml"); }

    @FXML
    void onClickBKcancel(MouseEvent event) { bkPane.setVisible(false); }

    @FXML
    void onClickBooking(MouseEvent event) {
        java.util.Date nowDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 투숙일, 퇴실일 입력이 되지 않은경우 반려
        if (datePickerStartDate.getValue() == null || datePickerEndDate.getValue() == null) setInfoModal("기간을 입력해 주세요.");
        else {
            String sdate = sdf.format(Date.valueOf(datePickerStartDate.getValue()));
            String edate = sdf.format(Date.valueOf(datePickerEndDate.getValue()));
            // 오늘 이전의 날짜는 예약이 불가능하게 설정
            if (sdate.compareTo(sdf.format(nowDate)) < 0) {
                setInfoModal("투숙일은 오늘보다 빠를 수 없습니다.");
                // 퇴실일이 투숙일과 같거나 빠른경우 반려
            } else
            if (edate.compareTo(sdate) <= 0) {
                setInfoModal("투숙일은 퇴실일보다 같거나 늦을 수 없습니다.");
            } else {
                int roomUseDay = (int) ((Date.valueOf(edate).getTime() - Date.valueOf(sdate).getTime())/86400000);
                // 예약 최종 확인창으로 데이터 전송
                bkModalAdressText.setText( businessName + " " + roomDetailName);
                bkModalDateText.setText(sdate + " ~ " + edate);
                bkModalPriceText.setText(roomDetailPrice * roomUseDay + "원( 총 " + roomUseDay +"박 )");
                dateModalPane.setVisible(false);
                bkPane.setVisible(true);
            }
        }


    }
    @FXML
    void onClickLeftImageChange(MouseEvent event) {
        if (nowImageNum > 0){
            nowImageNum--;
            imageView.setImage(new Image(imgUrl.get(nowImageNum)));
            centerImage();
            rightImageChange.setVisible(true);
            if (nowImageNum == 0) leftImageChange.setVisible(false);
        }
    }

    @FXML
    void onClickRightImageChange(MouseEvent event) {
        if (nowImageNum < imgUrl.size()-1){
            nowImageNum++;
            imageView.setImage(new Image(imgUrl.get(nowImageNum)));
            centerImage();
            leftImageChange.setVisible(true);
            if (nowImageNum== imgUrl.size()-1) rightImageChange.setVisible(false);
        }
    }

    @FXML
    void onClickBKAccept(MouseEvent event) {
        // 예약 중복체크
        String SQL = "SELECT 업체명, 호실, 숙소정원, 분류, 가격, 숙소번호, 주소 FROM everyhotel.숙소 natural join everyhotel.숙박업체 where 숙소.업체아이디=숙박업체.업체아이디" +
                " and 숙소.숙소번호 not in ( select 예약.숙소번호 " +
                "from everyhotel.예약 " +
                "where ( \'" + datePickerStartDate.getValue() +"\' between 예약.예약일 and 퇴실일) or " +
                "( \'"+ datePickerEndDate.getValue() +"\' between 예약.예약일 and 퇴실일 ) or " +
                "( 예약.예약일 between \'" + datePickerStartDate.getValue() +"\' and \' " + datePickerEndDate.getValue() + "\') or " +
                "( 예약.퇴실일 between \'" + datePickerStartDate.getValue() +"\' and \' " + datePickerEndDate.getValue() + "\')  )" +
                "and 숙소번호=" +roomNumber + ";";
        ResultSet result = dbc.sendQuryGet(SQL);
        try {
            if(result.next()){
                // 예약하는 쿼리문 쏘기
                LoginData ld = new LoginData();
                dbc.sendQuryPost("insert into everyhotel.예약(예약일, 퇴실일, 숙소번호, 회원아이디) values(\'" + sDate + "\', \'" + eDate + "\', " + roomNumber + ", \'" + ld.getId() + "\');");
                bkPane.setVisible(false);
                successBkPane.setVisible(true);
            }else {
                setInfoModal("이미 예약된 숙소 입니다.");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
