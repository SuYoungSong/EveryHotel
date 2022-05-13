package com.example.bookedroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class BusinessSearchBookingPageController extends Controller implements Initializable {

    @FXML private ScrollPane DateSearchPane;
    @FXML private ScrollPane roomSearchPane;
    @FXML private TableView dateTableView;

    @FXML private TableColumn tcRoomName;
    @FXML private TableColumn tcToday;
    @FXML private TableColumn tcNextday;
    @FXML private TableColumn tcYesterday;

    @FXML
    void moveBusinessMainPage(MouseEvent event) throws IOException {
        movePage(event, "BusinessMainPageUI.fxml");
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
        tcRoomName.setCellValueFactory(new PropertyValueFactory("roomName"));
        tcYesterday.setCellValueFactory(new PropertyValueFactory("yesterday"));
        tcToday.setCellValueFactory(new PropertyValueFactory("today"));
        tcNextday.setCellValueFactory(new PropertyValueFactory("nextday"));

        Label placeH = new Label("등록된 숙소가 없습니다.");
        placeH.setStyle("-fx-font-size: 20;");
        dateTableView.setPlaceholder((placeH));

        LoginData ld = new LoginData();
        VBox vbItems = new VBox(5);
        List<Pane> roomList = new ArrayList<Pane>();

        ResultSet result = dbc.sendQuryGet("SELECT 숙소번호,호실 FROM everyhotel.숙소 WHERE 업체아이디=\'"+ld.getId()+"\'");
        // 등록된 숙소가 있는 경우 추가해주기

        try {
            ArrayList<HashMap<String,Object>> room = convertResultSetToArrayList(result);
            for(HashMap<String,Object> hm : room) {
                int i = 70;

                Pane roomPane = new Pane();
                roomPane.setPrefWidth(300);
                roomPane.setPadding(new Insets(20));
                roomPane.setStyle("-fx-background-color: #EAAC41; -fx-background-radius: 5px;");


                int rnTemp = (int) hm.get("숙소번호");
                String roomNumber = (String) hm.get("호실");


                Label rTp = new Label(rnTemp + "");
                rTp.setVisible(false);

                Label rn = new Label(roomNumber);
                rn.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 40;");
                rn.setLayoutY(15);
                rn.setLayoutX(10);

                result = dbc.sendQuryGet("select 예약.예약일, 예약.퇴실일, 예약.회원아이디 from everyhotel.숙소 left outer join everyhotel.예약 on 숙소.숙소번호=예약.숙소번호 where 숙소.업체아이디=\'" + ld.getId() + "\' and 호실 =\'" + roomNumber + "\';");
                try {
                    while (result.next()) {
                        String mi = result.getString("회원아이디");
                        Date sd = result.getDate("예약일");
                        Date ed = result.getDate("퇴실일");

                        Label memberId = new Label(mi);
                        memberId.setStyle("-fx-font-size:20; -fx-text-fill: #ffffff");
                        memberId.setLayoutX(10);
                        memberId.setLayoutY(i);

                        Label temp = new Label(sd.toString() + "~" + ed.toString());
                        temp.setStyle("-fx-font-size:20; -fx-text-fill: #ffffff");
                        temp.setLayoutX(130);
                        temp.setLayoutY(i);
                        i += 20;
                        roomPane.getChildren().addAll(memberId, temp);
                    }
                }catch(Exception e2){
                        System.out.println(e2.getMessage());
                        Label temp = new Label("예약자가 없습니다.");
                        temp.setStyle("-fx-font-size:20; -fx-text-fill: #ffffff");
                        temp.setLayoutX(10);
                        temp.setLayoutY(i);
                        roomPane.getChildren().add(temp);
                    }

                    roomPane.setPrefHeight(30 + i);
                    roomPane.getChildren().addAll(rTp, rn);
                    vbItems.setMargin(roomPane, new Insets(12));
                    roomList.add(roomPane);
                }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        roomSearchPane.setContent(vbItems);
        roomSearchPane.setFitToWidth(true);

        if (roomList.size()>0) {
            for (Pane ii : roomList) {
                vbItems.getChildren().add(ii);
            }
        }else {
            Label temp = new Label("등록된 숙소가 없습니다.");
            temp.setStyle("-fx-font-size: 20;");
            temp.setLayoutX(70);
            temp.setLayoutY(180);
            Pane pTemp = new Pane();
            pTemp.getChildren().add(temp);
            vbItems.getChildren().add(pTemp);
            roomSearchPane.setContent(vbItems);
            roomSearchPane.setFitToWidth(true);
        }
        // 행열 바꿔서 통계 내기
        // 참고 : https://hyunmin1906.tistory.com/274
        result = dbc.sendQuryGet(
                "select 숙소.호실," +
                "coalesce(case when 예약.예약일=Date(now() - interval 1 day) THEN count(예약.예약번호) END,0) AS 어제,"+
                "coalesce(case when 예약.예약일=Date(now()) THEN count(예약.예약번호) END,0) AS 오늘,"+
                "coalesce(case when 예약.예약일=Date(now() + interval 1 day) THEN count(예약.예약번호) END,0) AS 내일 "+
                "from everyhotel.예약, everyhotel.숙소 "+
                "where 예약.숙소번호=숙소.숙소번호 and 숙소.업체아이디=\'"+ld.getId()+"\' "+
                "group by 예약.숙소번호 "+
                "order by 예약.숙소번호"+";"
        );


        try {
            while (result.next()) {
                String roomName = result.getString("호실");
                int yesterday = result.getInt("어제");
                int today = result.getInt("오늘");
                int nextday =  result.getInt("내일");
                dateTableView.getItems().add(new DateFind(roomName, yesterday, today, nextday));
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
