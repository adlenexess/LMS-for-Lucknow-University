module com.lu.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens com.lu.lms to javafx.fxml;
    opens com.lu.lms.controller to javafx.fxml;
    exports com.lu.lms;
}
