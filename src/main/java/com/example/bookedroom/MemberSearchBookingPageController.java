package com.example.bookedroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MemberSearchBookingPageController extends Controller implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private Pane reviewPane;
    @FXML private TextArea contentText;
    @FXML private ChoiceBox<String> star;
    Button tempBtn;
    Node nd;

    @FXML
    void onClickReviewWrite(MouseEvent event) throws SQLException, IOException {
        LoginData ld = new LoginData();
        System.out.println(nd.getParent().getChildrenUnmodifiable());
        System.out.println(nd.getParent().getChildrenUnmodifiable().size());
        Label rnb = (Label) nd.getParent().getChildrenUnmodifiable().get(1);
        Label sd = (Label) nd.getParent().getChildrenUnmodifiable().get(5);
        Label ed = (Label) nd.getParent().getChildrenUnmodifiable().get(6);

        int score = star.getValue().length();
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String ct = contentText.getText();
        if (ct.length()>100) ct = ct.substring(0,101);

        // 리뷰작성인 경우
        if (tempBtn.getText().equals("리뷰작성")){
            dbc.sendQuryPost("insert into everyhotel.리뷰(리뷰내용,평점,작성일자,숙소번호) values"+ "(\'" + ct + "\'," + score + ",\'" + sdf.format(nowDate) + "\'," + Integer.valueOf(rnb.getText()) + ");" );
            // 리뷰 수정인 경우
        }else{
            ResultSet result = dbc.sendQuryGet("select everyhotel.리뷰.리뷰번호 from everyhotel.리뷰,everyhotel.예약 where everyhotel.예약.숙소번호="+ Integer.valueOf(rnb.getText()) +" and everyhotel.예약.예약일=\'" + sd.getText().substring(6) + "\' and everyhotel.예약.퇴실일=\'" + ed.getText().substring(6) + "\' and everyhotel.예약.회원아이디=\'" + ld.getId() + "\';");
            result.next();
            int reviewNum = result.getInt("리뷰번호");
            dbc.sendQuryPost("update everyhotel.리뷰 set 리뷰내용=\'"+ ct + "\', 평점="+score+", 작성일자=\'" + sdf.format(nowDate) + "\' where 리뷰번호=" + reviewNum + ";");
        }

        movePage(event,"MemberSearchBookinPageUI.fxml");
    }

    @FXML
    void moveMainPage(MouseEvent event) throws IOException {
        movePage(event, "MemberMainPageUI.fxml");
    }

    public ArrayList<HashMap<String,Object>> convertResultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

        while(rs.next()) {
            HashMap<String,Object> row = new HashMap<String, Object>(columns);
            for(int i=1; i<=columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> tem = FXCollections.observableArrayList("★","★★","★★★","★★★★","★★★★★");
        star.setItems(tem);
        star.setValue("★");

        Date time = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            time = sdf.parse(sdf.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<HashMap<String,Object>> rs;
        LoginData ld = new LoginData();
        VBox vbItems = new VBox(5);
        List<Pane> roomList = new ArrayList<Pane>();
        ResultSet result = dbc.sendQuryGet("select everyhotel.예약.예약번호, everyhotel.예약.숙소번호, everyhotel.숙박업체.업체명, everyhotel.숙소.호실, everyhotel.숙소.가격, everyhotel.숙소.숙소정원, everyhotel.예약.예약일, everyhotel.예약.퇴실일\n" +
                "from everyhotel.숙박업체, everyhotel.숙소, everyhotel.예약\n" +
                "where everyhotel.예약.숙소번호=everyhotel.숙소.숙소번호 and everyhotel.숙박업체.업체아이디=everyhotel.숙소.업체아이디 and everyhotel.예약.회원아이디=\'" + ld.getId() + "\'");
        try {
            rs = convertResultSetToArrayList(result);
            Iterator iter = rs.iterator();

            while (iter.hasNext()) {
                HashMap<String, Object> hmap = (HashMap<String, Object>) iter.next();
                Pane roomPane = new Pane();
                roomPane.setPrefWidth(300);
                roomPane.setPrefHeight(190);
                roomPane.setPadding(new Insets(20));
                roomPane.setStyle("-fx-background-color: #EAAC41; -fx-background-radius: 5px;");

                int bookingNum = (int) hmap.get("예약번호");
                int roomNum = (int) hmap.get("숙소번호");
                String businessName = (String) hmap.get("업체명");
                String roomName = businessName + " " + hmap.get("호실");
                int roomPrice = (int) hmap.get("가격");
                int roomMaxPeople = (int) hmap.get("숙소정원");
                Date startDate = (Date) hmap.get("예약일");
                Date endDate = (Date) hmap.get("퇴실일");


                // 날짜.compareTo(뒷날짜)     뒷 날짜가 큰 경우  음수, 앞 날짜가 큰 경우 양수, 같을경우 0
                int startDateCompare = startDate.compareTo(time);
                int endDateCompare = endDate.compareTo(time);

                Label bn = new Label(""+bookingNum);
                bn.setVisible(false);

                Label rnb = new Label(""+roomNum);
                rnb.setVisible(false);

                Label rn = new Label(roomName);
                rn.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 30;");
                rn.setLayoutX(15);
                rn.setLayoutY(15);

                Label mp = new Label("정원 :  " + roomMaxPeople);
                mp.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                mp.setLayoutX(15);
                mp.setLayoutY(45);

                Label rp = new Label("가격 :  " + roomPrice);
                rp.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                rp.setLayoutX(100);
                rp.setLayoutY(45);

                Label sd = new Label("예약일 :  " + startDate);
                sd.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                sd.setLayoutX(15);
                sd.setLayoutY(75);

                Label ed = new Label("퇴실일 :  " + endDate);
                ed.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                ed.setLayoutX(15);
                ed.setLayoutY(105);

                Button writeReview = new Button("리뷰작성");
                writeReview.setLayoutX(20);
                writeReview.setLayoutY(150);
                writeReview.setPrefHeight(30);
                writeReview.setPrefWidth(140);
                writeReview.setOnMouseClicked(event ->onClickWriteReview(event));

                Button cancelBooking = new Button("예약취소");
                cancelBooking.setLayoutX(160);
                cancelBooking.setLayoutY(150);
                cancelBooking.setPrefHeight(30);
                cancelBooking.setPrefWidth(140);
                cancelBooking.setOnMouseClicked(event -> onClickcancelBooking(event));

                // 예약일을 지난경우
                if (startDateCompare<0){
                    // 예약 취소버튼 제거
                    cancelBooking.setVisible(false);
                    writeReview.setPrefWidth(280);
                }

                // 퇴실일 이전일 경우
                if (endDateCompare>0){
                    // 리뷰작성 버튼 제거
                    writeReview.setVisible(false);
                    cancelBooking.setLayoutX(20);
                    cancelBooking.setPrefWidth(280);
                } else {
                        //퇴실일 이후인 경우
                        ResultSet result2 = dbc.sendQuryGet("select * from everyhotel.리뷰 left outer join everyhotel.예약 on everyhotel.리뷰.숙소번호=everyhotel.예약.숙소번호 where everyhotel.예약.예약번호=" + bn.getText() + ";");
                    if( (result2.next())) {
                        //이미 등록된 리뷰가 있는경우 리뷰 수정으로 버튼 변경
                        writeReview.setText("리뷰수정");
                    }
                }

                roomPane.getChildren().addAll(bn, rnb, rn, mp, rp, sd,ed, writeReview, cancelBooking);
                vbItems.setMargin(roomPane, new Insets(12));
                roomList.add(roomPane);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        scrollPane.setContent(vbItems);
        scrollPane.setFitToWidth(true);

        if (roomList.size() > 0) {
            for (Pane i : roomList) {
                vbItems.getChildren().add(i);
            }
        } else {
            Label temp = new Label("예약 정보가 없습니다.");
            temp.setStyle("-fx-font-size: 20;");
            temp.setLayoutX(70);
            temp.setLayoutY(180);
            Pane pTemp = new Pane();
            pTemp.getChildren().add(temp);
            vbItems.getChildren().add(pTemp);
            scrollPane.setContent(vbItems);
            scrollPane.setFitToWidth(true);
        }
    }

    private void onClickcancelBooking(MouseEvent event) {
        Node btn = (Node) event.getSource();
        Label bn = (Label)btn.getParent().getChildrenUnmodifiable().get(0);
        dbc.sendQuryPost("delete from everyhotel.예약 where 예약번호=+"+bn.getText()+";");
        try { movePage(event, "MemberSearchBookinPageUI.fxml"); } catch (IOException e) { e.printStackTrace(); }
    }

    private void onClickWriteReview(MouseEvent event) {
        tempBtn = (Button) event.getSource();
        nd = (Node) event.getSource();
        Label bn = (Label)nd.getParent().getChildrenUnmodifiable().get(0);
        reviewPane.setVisible(true);
        try {
            if (tempBtn.getText().equals("리뷰수정")) {
                ResultSet result = dbc.sendQuryGet("select 리뷰내용, 평점 from everyhotel.리뷰 left outer join everyhotel.예약 on everyhotel.리뷰.숙소번호=everyhotel.예약.숙소번호 where everyhotel.예약.예약번호=" + Integer.valueOf(bn.getText()) + ";");
                result.next();
                int sn = result.getInt("평점");
                String tp = "";
                for (int i = 0; i < sn; i++) {
                    tp += "★";
                }
                star.setValue(tp);
                contentText.setText(result.getString("리뷰내용"));
            }
        }catch(Exception e){
                System.out.println(e.getMessage());
        }
    }
}