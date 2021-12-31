module edu.packagemanagement {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.xml;
	requires java.desktop;
	requires json.simple;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;

    opens edu.packagemanagement to javafx.fxml;
    exports edu.packagemanagement;
}
