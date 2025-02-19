module com.example.bloc4 {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires net.synedra.validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;
	requires com.google.gson;
	requires java.sql;

	opens com.example.bloc4 to javafx.fxml;
	opens com.example.bloc4.controller to javafx.fxml;
	opens com.example.bloc4.model to com.google.gson, javafx.base;
	exports com.example.bloc4;
	exports com.example.bloc4.controller;
}