module org.example.thegreatests {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.thegreatests to javafx.fxml;
    exports org.example.thegreatests;
}