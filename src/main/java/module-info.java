module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
    exports org.openjfx.javafx;
    opens org.openjfx.javafx to javafx.fxml;
    exports org.openjfx.puzzle;
    opens org.openjfx.puzzle to javafx.fxml;
    exports org.openjfx.benchmark;
    opens org.openjfx.benchmark to javafx.fxml;
}