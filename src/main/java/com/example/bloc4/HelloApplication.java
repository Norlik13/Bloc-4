package com.example.bloc4;

import com.example.bloc4.controller.SalarieController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("salarie-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/bloc4/Style.css")).toExternalForm());
		stage.setTitle("Annuaire des salariés");
		stage.setScene(scene);
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/bloc4/icon.jpg"))));
		stage.show();

		SalarieController controller = fxmlLoader.getController();
		scene.addEventFilter(KeyEvent.KEY_PRESSED, controller::handleKeyPress);
	}

	public static void main(String[] args) {
		launch();
	}
}