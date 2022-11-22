module com.example.coursebankdeposit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;


    opens com.example.coursebankdeposit to javafx.fxml;
    exports com.example.coursebankdeposit;
    exports com.example.coursebankdeposit.Controller;
    opens com.example.coursebankdeposit.Controller to javafx.fxml;
}