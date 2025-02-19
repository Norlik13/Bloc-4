package com.example.bloc4.controller;

import com.example.bloc4.model.Salarie;
import com.example.bloc4.model.Department;
import com.example.bloc4.model.Site;
import com.example.bloc4.service.SalarieService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifySalarieController {

	@FXML
	private TextField nomField;
	@FXML
	private TextField prenomField;
	@FXML
	private TextField telephoneFixeField;
	@FXML
	private TextField telephonePortableField;
	@FXML
	private TextField emailField;
	@FXML
	private ComboBox<Department> serviceComboBox;
	@FXML
	private ComboBox<Site> siteComboBox;

	private Stage dialogStage;
	private Salarie salarie;
	private boolean saveClicked = false;
	private SalarieService salarieService = new SalarieService();

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setSalarie(Salarie salarie) {
		this.salarie = salarie;

		nomField.setText(salarie.getNom());
		prenomField.setText(salarie.getPrenom());
		telephoneFixeField.setText(salarie.getTelephoneFixe());
		telephonePortableField.setText(salarie.getTelephonePortable());
		emailField.setText(salarie.getEmail());
		serviceComboBox.setValue(salarie.getDepartment());
		siteComboBox.setValue(salarie.getSite());
	}

	public boolean isSaveClicked() {
		return saveClicked;
	}

	@FXML
	private void handleSave() {
		salarie.setNom(nomField.getText());
		salarie.setPrenom(prenomField.getText());
		salarie.setTelephoneFixe(telephoneFixeField.getText());
		salarie.setTelephonePortable(telephonePortableField.getText());
		salarie.setEmail(emailField.getText());
		salarie.setDepartment(serviceComboBox.getValue());
		salarie.setSite(siteComboBox.getValue());

		try {
			salarieService.updateSalarieInAPI(salarie);
			saveClicked = true;
			dialogStage.close();
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to update salarie");
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}