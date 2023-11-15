module com.example.fxcheck {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens com.example.fxcheck to javafx.fxml;
    exports com.example.fxcheck;
}