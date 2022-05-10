package com.example.bookedroom;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

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
    @FXML private TextField roomNumberText;
    @FXML private TextField priceText;
    @FXML private Pane errorModal;
    @FXML private Text errorText;
    ArrayList<String> imUrl = new ArrayList<>();
    LoginData ld = new LoginData();

    void addRoomModalInit(){
        addRoomModal.setVisible(false);
        imageUrlText.setText("");
        maxPeopleNum.setText("");
        roomNumberText.setText("");
        priceText.setText("");
        imUrl = null;
        ObservableList<String> imUrlList = FXCollections.observableList(imUrl);
        imageUrlList.setItems(imUrlList);
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
        imageUrlText.setText(String.valueOf(selectedFile));
        imUrl.add(String.valueOf(selectedFile));
        ObservableList<String> imUrlList = FXCollections.observableList(imUrl);
        imageUrlList.setItems(imUrlList);
    }
    @FXML
    void moveMainPage(MouseEvent event) throws IOException {
        movePage(event, "BusinessMainPageUI.fxml");
    }
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
        ObservableList<String> boxList = FXCollections.observableArrayList("호텔","모텔","리조트","야영장","캠핑카","민박");
        roomTypeBox.setItems(boxList);
        roomTypeBox.setValue("호텔");


        // 등록된 숙소가 있는 경우 추가해주기
    }
}
