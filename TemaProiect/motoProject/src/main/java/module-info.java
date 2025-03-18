module org.example.motoproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.sql;


    opens org.example.motoproject to javafx.fxml;
    exports org.example.motoproject;
}