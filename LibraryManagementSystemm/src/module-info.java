module LibraryManagementSystemm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens Library to javafx.fxml;
    exports Library;

    opens Library.ui to javafx.fxml;
    exports Library.ui;

    opens Library.model to javafx.fxml;
    exports Library.model;

    opens Library.service to javafx.fxml;
    exports Library.service;

    opens Library.util to javafx.fxml;
    exports Library.util;
}
