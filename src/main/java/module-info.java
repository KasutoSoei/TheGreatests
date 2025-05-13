module org.example.thegreatests {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.thegreatests to javafx.fxml;
    opens org.example.thegreatests.Views to javafx.fxml;
    opens org.example.thegreatests.Controllers to javafx.fxml;

    exports org.example.thegreatests.Models;
    exports org.example.thegreatests.Views;
    exports org.example.thegreatests.Controllers;
}
