package com.example.bloc4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModificationDialogController {

	@FXML
	private TextField nameField;

	private Stage dialogStage;
	private boolean saveClicked = false;
	private String newName;

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setName(String name) {
		nameField.setText(name);
	}

	public String getNewName() {
		return newName;
	}

	public boolean isSaveClicked() {
		return saveClicked;
	}

	@FXML
	private void handleSave() {
		newName = nameField.getText();
		saveClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}