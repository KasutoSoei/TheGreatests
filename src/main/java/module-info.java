module org.example.thegreatests {
    requires javafx.controls;
    requires javafx.fxml;
    requires ormlite.jdbc;
    requires java.sql;

    opens org.example.thegreatests to javafx.fxml;
    opens org.example.thegreatests.Views to javafx.fxml;
    opens org.example.thegreatests.Controllers to javafx.fxml, ormlite.jdbc;
    opens org.example.thegreatests.Models to ormlite.jdbc;

    exports org.example.thegreatests.Models;
    exports org.example.thegreatests.Views;
    exports org.example.thegreatests.Controllers;
}
