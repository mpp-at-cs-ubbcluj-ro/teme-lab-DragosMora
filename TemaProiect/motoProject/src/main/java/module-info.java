module org.example.motoproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.motoproject to javafx.fxml;
    exports org.example.motoproject;
}