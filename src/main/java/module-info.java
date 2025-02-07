module com.sprain6628.background_adder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.sprain6628.background_adder to javafx.fxml;
    exports com.sprain6628.background_adder;
}