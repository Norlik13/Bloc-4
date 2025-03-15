package com.example.bloc4.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class SelectionDialogController<T> {

	@FXML
	private ComboBox<T> selectionComboBox;

	private Stage dialogStage;
	private boolean deleteClicked = false;

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setItems(ObservableList<T> items) {
		selectionComboBox.setItems(items);
	}

	public T getSelectedItem() {
		return selectionComboBox.getValue();
	}

	public boolean isDeleteClicked() {
		return deleteClicked;
	}

	@FXML
	private void handleDelete() {
		deleteClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}