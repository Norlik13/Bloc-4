package com.example.bloc4.controller;

import com.example.bloc4.model.Salarie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SalarieDetailsController {

	@FXML
	private Label nomLabel;
	@FXML
	private Label prenomLabel;
	@FXML
	private Label telephoneFixeLabel;
	@FXML
	private Label telephonePortableLabel;
	@FXML
	private Label emailLabel;
	@FXML
	private Label departmentLabel;
	@FXML
	private Label siteLabel;

	private Stage dialogStage;

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setSalarie(Salarie salarie) {
		nomLabel.setText("Nom: " + salarie.getNom());
		prenomLabel.setText("Prenom: " + salarie.getPrenom());
		telephoneFixeLabel.setText("Telephone Fixe: " + salarie.getTelephoneFixe());
		telephonePortableLabel.setText("Telephone Portable: " + salarie.getTelephonePortable());
		emailLabel.setText("Email: " + salarie.getEmail());
		departmentLabel.setText("Department: " + salarie.getDepartment().getName());
		siteLabel.setText("Site: " + salarie.getSite().getName());
	}
}