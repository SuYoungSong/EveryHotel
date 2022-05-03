module com.example.bookedroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    opens com.example.bookedroom to javafx.fxml;
    exports com.example.bookedroom;
}