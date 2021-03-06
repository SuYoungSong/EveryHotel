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
        // ????????? ?????? ??????
        ResultSet result = dbc.sendQuryGet("select ?????????, ??????, ??????, ??????, ????????????, ?????????, ??????????????? from everyhotel.?????? natural join everyhotel.???????????? where ????????????="+roomNumber+";");
        try {
            result.next();
            businessName = result.getString("?????????");
            roomDetailName = result.getString("??????");
            roomDetailPrice = result.getInt("??????");
            businessAdress = result.getString("??????");
            roomDetailMaxPeople = result.getInt("????????????");
            businessPhone = result.getString("?????????");
            businessNumber = result.getString("???????????????");

            roomName.setText(businessName + " " + roomDetailName);
            roomAddress.setText(businessAdress);
            roomPrice.setText("1??? " + roomDetailPrice + "???");
            maxPeopleNumText.setText(roomDetailMaxPeople + "???");
            companyPhone.setText(businessPhone);
            companyId.setText(businessNumber);

        } catch (SQLException e) {
            System.out.println("?????? ?????? ?????? ??????");
            System.out.println(e.getMessage());
        }
    }

    void reviewSetting(){
        // ?????? ?????? ??????
        VBox vbItems = new VBox(5);
        List<Pane> roomList = new ArrayList<Pane>();
        ResultSet result = dbc.sendQuryGet("select ???????????????, ????????????, ?????? from everyhotel.?????? natural join everyhotel.?????? where ??????.????????????=??????.???????????? and ????????????="+ roomNumber+";");

        try {
            while (result.next()) {
                String st = "";
                Pane roomPane = new Pane();
                roomPane.setPrefWidth(300);
                roomPane.setPrefHeight(68);
                roomPane.setPadding(new Insets(20));
                roomPane.setStyle("-fx-background-color: #C670E7; -fx-background-radius: 5px;");

                String memberId = result.getString("???????????????");
                String reviewContent = result.getString("????????????");
                int star = result.getInt("??????");

                Label mi = new Label("?????????: "+memberId);
                mi.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                mi.setLayoutX(10);
                mi.setLayoutY(15);

                Label rc = new Label(reviewContent);
                rc.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15;");
                rc.setLayoutX(10);
                rc.setLayoutY(33);

                for( int i = 0 ; i<star ; i++)
                    st += "???";

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
            Label temp = new Label("????????? ????????? ????????????.");
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
        ResultSet result = dbc.sendQuryGet("select ??????????????? from everyhotel.????????? where ????????????="+ roomNumber +";");
        try {
            while (result.next()){
                imgUrl.add(result.getString("???????????????"));
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
        // ????????? ???????????? ??????????????? ?????? ????????????
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
        // ?????????, ????????? ????????? ?????? ???????????? ??????
        if (datePickerStartDate.getValue() == null || datePickerEndDate.getValue() == null) setInfoModal("????????? ????????? ?????????.");
        else {
            String sdate = sdf.format(Date.valueOf(datePickerStartDate.getValue()));
            String edate = sdf.format(Date.valueOf(datePickerEndDate.getValue()));
            // ?????? ????????? ????????? ????????? ??????????????? ??????
            if (sdate.compareTo(sdf.format(nowDate)) < 0) {
                setInfoModal("???????????? ???????????? ?????? ??? ????????????.");
                // ???????????? ???????????? ????????? ???????????? ??????
            } else
            if (edate.compareTo(sdate) <= 0) {
                setInfoModal("???????????? ??????????????? ????????? ?????? ??? ????????????.");
            } else {
                int roomUseDay = (int) ((Date.valueOf(edate).getTime() - Date.valueOf(sdate).getTime())/86400000);
                // ?????? ?????? ??????????????? ????????? ??????
                bkModalAdressText.setText( businessName + " " + roomDetailName);
                bkModalDateText.setText(sdate + " ~ " + edate);
                bkModalPriceText.setText(roomDetailPrice * roomUseDay + "???( ??? " + roomUseDay +"??? )");
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
        // ?????? ????????????
        String SQL = "SELECT ?????????, ??????, ????????????, ??????, ??????, ????????????, ?????? FROM everyhotel.?????? natural join everyhotel.???????????? where ??????.???????????????=????????????.???????????????" +
                " and ??????.???????????? not in ( select ??????.???????????? " +
                "from everyhotel.?????? " +
                "where ( \'" + datePickerStartDate.getValue() +"\' between ??????.????????? and ?????????) or " +
                "( \'"+ datePickerEndDate.getValue() +"\' between ??????.????????? and ????????? ) or " +
                "( ??????.????????? between \'" + datePickerStartDate.getValue() +"\' and \' " + datePickerEndDate.getValue() + "\') or " +
                "( ??????.????????? between \'" + datePickerStartDate.getValue() +"\' and \' " + datePickerEndDate.getValue() + "\')  )" +
                "and ????????????=" +roomNumber + ";";
        ResultSet result = dbc.sendQuryGet(SQL);
        try {
            if(result.next()){
                // ???????????? ????????? ??????
                LoginData ld = new LoginData();
                dbc.sendQuryPost("insert into everyhotel.??????(?????????, ?????????, ????????????, ???????????????) values(\'" + sDate + "\', \'" + eDate + "\', " + roomNumber + ", \'" + ld.getId() + "\');");
                bkPane.setVisible(false);
                successBkPane.setVisible(true);
            }else {
                setInfoModal("?????? ????????? ?????? ?????????.");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
