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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ControlBookingPageController extends Controller implements Initializable {
    @FXML private TextField imageUrlText;
    @FXML private ListView<String> imageUrlList;
    @FXML private TextField maxPeopleNum;
    @FXML private Pane addRoomModal;
    @FXML private ChoiceBox<String> roomTypeBox;
    @FXML private ComboBox<String> ciRoomType;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField roomNumberText;
    @FXML private TextField priceText;
    @FXML private Pane errorModal;
    @FXML private Text errorText;
    @FXML private Pane changeRoomInfoPane;
    @FXML private TextField ciMaxPeople;
    @FXML private TextField ciRoomNumber;
    @FXML private TextField ciPrice;
    @FXML private ListView<String> ciImageList;

    ArrayList<String> imUrl = new ArrayList<>();
    ArrayList<String> ciImUrl = new ArrayList<>();
    LoginData ld = new LoginData();
    int tprn;

    void enableInfoPane(MouseEvent event){
        Node nd = (Node) event.getTarget();
        Label rn = (Label) nd.getParent().getChildrenUnmodifiable().get(0);
        tprn = Integer.valueOf(rn.getText());
        try {
            ResultSet result = dbc.sendQuryGet("select * from everyhotel.숙소 where 숙소번호=" + tprn + ";");
            result.next();

            int ciRN = result.getInt("숙소번호");
            int ciPr = result.getInt("가격");
            String ciRT = result.getString("분류");
            String ciDRN = result.getString("호실");
            int ciMP = result.getInt("숙소정원");

            ciRoomNumber.setText(ciDRN);
            ciRoomType.setValue(ciRT);
            ciMaxPeople.setText(ciMP+"");
            ciPrice.setText(ciPr+"");

            result = dbc.sendQuryGet("SELECT * FROM everyhotel.이미지 where 숙소번호=" + tprn + ";");
            while(result.next()){
                ciImUrl.add(result.getString("이미지주소"));
            }

            if (ciImUrl.size()>0){
                ObservableList<String> tp = FXCollections.observableList(ciImUrl);
                ciImageList.setItems(tp);
            }
            changeRoomInfoPane.setVisible(true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void onClickCiImageAdd(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("이미지 선택");
        fc.setInitialDirectory(new File("C:/")); // 기본 디렉토리

        // 확장자 제한
        FileChooser.ExtensionFilter imgType = new FileChooser.ExtensionFilter("image file", "*.jpg","*.gif","*.png");
        fc.getExtensionFilters().add(imgType);
        File selectedFile = fc.showOpenDialog(null); // 창을 특정 위치에 띄운다. null==기본
        if (selectedFile != null) {
            ciImUrl.add(String.valueOf(selectedFile));
            ObservableList<String> imUrlList = FXCollections.observableList(ciImUrl);
            ciImageList.setItems(imUrlList);
        }
    }

    @FXML
    void onClickCiImageDel(MouseEvent event) {
        String selItem = ciImageList.getSelectionModel().getSelectedItem();
        if (ciImUrl.size()>0) {
            ciImUrl.remove(selItem);
            ObservableList<String> imUrlList = FXCollections.observableList(ciImUrl);
            ciImageList.setItems(imUrlList);
        }
    }

    @FXML
    void onClickChangeRoomInfo(MouseEvent event) throws IOException {

        int result = dbc.sendQuryPost("update everyhotel.숙소 set 가격=\'" + Integer.valueOf(ciPrice.getText()) + "\', 분류=\'" + ciRoomType.getValue() +
                "\', 호실=\'" + ciRoomNumber.getText() + "\', 숙소정원=" + Integer.valueOf(ciMaxPeople.getText()) + " where 숙소번호 =" + tprn + ";");

        if (result!=0) {
            setErrorMsg("입력이 잘못되었습니다.");
        }else {
            changeRoomInfoPane.setVisible(false);
        }
        dbc.sendQuryPost("delete from everyhotel.이미지 where 숙소번호="+ tprn + ";");
        for( String i : ciImUrl){
            dbc.sendQuryPost("insert into everyhotel.이미지(숙소번호, 이미지주소) values(" + tprn +", \'" + i +"\');");
        }

        movePage(event,"ControlBookingPageUI.fxml");
    }

    @FXML
    void onClickCloseAddRoom(MouseEvent event) { addRoomModal.setVisible(false); }

    @FXML
    void onClickImageFind(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("이미지 선택");
        fc.setInitialDirectory(new File("C:/")); // 기본 디렉토리

        // 확장자 제한
        FileChooser.ExtensionFilter imgType = new FileChooser.ExtensionFilter("image file", "*.jpg","*.gif","*.png");
        fc.getExtensionFilters().add(imgType);
        File selectedFile = fc.showOpenDialog(null); // 창을 특정 위치에 띄운다. null==기본
        if (selectedFile != null) {
            imageUrlText.setText(String.valueOf(selectedFile));
            imUrl.add(String.valueOf(selectedFile));
            ObservableList<String> imUrlList = FXCollections.observableList(imUrl);
            imageUrlList.setItems(imUrlList);
        }
    }

    @FXML
    void moveMainPage(MouseEvent event) throws IOException { movePage(event, "BusinessMainPageUI.fxml"); }

    void setErrorMsg(String msg){ errorText.setText(msg); }

    @FXML
    void onClickAddRoom(MouseEvent event) { addRoomModal.setVisible(true); }

    @FXML
    void onClickAddRoomDB(MouseEvent event) throws SQLException, IOException {
        // 입력하지 않은 칸이 있는지 체크
        if(roomNumberText.getText().equals("")||priceText.getText().equals("")||maxPeopleNum.getText().equals("")){
            setErrorMsg("입력하지 않은 항목이 있습니다.");
            errorModal.setVisible(true);
            return;
        // 동일한 호실이 있는지 체크
        }else{
            ResultSet result = dbc.sendQuryGet("SELECT 호실 FROM everyhotel.숙소 WHERE 호실=\'"+ roomNumberText.getText() +"\' AND 업체아이디=\'"+ ld.getId() +"\';");
            if (result.next()) {
                setErrorMsg("이미 등록된 숙소입니다.");
                errorModal.setVisible(true);
                return;
            }
        }
        // DB에 추가하기
        dbc.sendQuryPost("INSERT INTO everyhotel.숙소(가격,분류,호실,숙소정원,업체아이디) VALUES("+Integer.valueOf(priceText.getText())+", \'"+roomTypeBox.getValue()+"\', \'"+roomNumberText.getText()+"\', "+Integer.valueOf(maxPeopleNum.getText())+", \'"+ld.getId()+"\');");

        // 이미지를 입력했는지 체크
        if(imUrl.size()>0){
            ResultSet result = dbc.sendQuryGet("SELECT 숙소번호 FROM everyhotel.숙소 WHERE 호실=\'"+roomNumberText.getText() +"\' AND 업체아이디=\'"+ ld.getId() +"\';");
            if (result.next()){
                int roomNumber = result.getInt(1);
                // 이미지 추가
                for(String i : imUrl){
                   dbc.sendQuryPost("INSERT INTO everyhotel.이미지(숙소번호,이미지주소) VALUES("+roomNumber+", \'"+i+"\');");
               }
            }
        }
        movePage(event,"ControlBookingPageUI.fxml");
    }
    @FXML
    void onClickCloseErrModal(MouseEvent event) { errorModal.setVisible(false); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoginData ld = new LoginData();
        ObservableList<String> boxList = FXCollections.observableArrayList("호텔","모텔","리조트","야영장","캠핑카","민박");
        ciRoomType.setItems(boxList);
        roomTypeBox.setItems(boxList);
        roomTypeBox.setValue("호텔");
        VBox vbItems = new VBox(5);
        List<Pane> roomList = new ArrayList<Pane>();

        ResultSet result = dbc.sendQuryGet("SELECT 숙소번호, 가격, 분류, 호실, 숙소정원 FROM everyhotel.숙소 WHERE 업체아이디=\'"+ld.getId()+"\'");
        // 등록된 숙소가 있는 경우 추가해주기
        try {
            while (result.next()) {
                Pane roomPane = new Pane();
                roomPane.setPrefWidth(300);
                roomPane.setPrefHeight(85);
                roomPane.setPadding(new Insets(20));
                roomPane.setStyle("-fx-background-color: #EAAC41; -fx-background-radius: 5px;");

                int rnTemp = result.getInt("숙소번호");
                String roomNumber = result.getString("호실");
                int roomPrice = result.getInt("가격");
                int maxPeopleNum = result.getInt("숙소정원");
                String roomType = result.getString("분류");

                Label rTp = new Label(rnTemp+"");
                rTp.setVisible(false);

                Label rn = new Label(roomNumber);
                rn.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 40;");
                rn.setLayoutY(15);
                rn.setLayoutX(10);

                Label mp = new Label("숙소정원 :  " + maxPeopleNum);
                mp.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                mp.setLayoutX(120);
                mp.setLayoutY(10);

                Label tp = new Label("분류 :  " + roomType);
                tp.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                tp.setLayoutX(120);
                tp.setLayoutY(30);

                Label pr = new Label("가격 :  " + roomPrice);
                pr.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");
                pr.setLayoutX(120);
                pr.setLayoutY(50);

                Label cpTemp = new Label();
                cpTemp.setOnMouseClicked(event -> {enableInfoPane(event);});
                cpTemp.setPrefWidth(300);
                cpTemp.setPrefHeight(85);
                cpTemp.setPadding(new Insets(20));
                cpTemp.setStyle("-fx-background-radius: 5px;");

                roomPane.getChildren().addAll(rTp, rn,mp,tp,pr, cpTemp);
                vbItems.setMargin(roomPane, new Insets(12));
                roomList.add(roomPane);


            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        scrollPane.setContent(vbItems);
        scrollPane.setFitToWidth(true);

        if (roomList.size()>0) {
            for (Pane i : roomList) {
                vbItems.getChildren().add(i);
            }
        }else {
            Label temp = new Label("등록된 숙소가 없습니다.");
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
}
