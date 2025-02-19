package com.example.bloc4.controller;

import com.example.bloc4.model.Department;
import com.example.bloc4.model.Salarie;
import com.example.bloc4.model.Site;
import com.example.bloc4.service.DepartmentService;
import com.example.bloc4.service.SiteService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AddSalarieController {

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

	@FXML
	private void initialize() {
		try {
			List<Site> sites = new SiteService().fetchSitesFromAPI();
			siteComboBox.getItems().addAll(sites);

			List<Department> departments = new DepartmentService().fetchDepartmentsFromAPI();
			serviceComboBox.getItems().addAll(departments);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isSaveClicked() {
		return saveClicked;
	}

	@FXML
	private void handleSave() {
		salarie = new Salarie();
		salarie.setNom(nomField.getText());
		salarie.setPrenom(prenomField.getText());
		salarie.setTelephoneFixe(telephoneFixeField.getText());
		salarie.setTelephonePortable(telephonePortableField.getText());
		salarie.setEmail(emailField.getText());
		salarie.setDepartment(serviceComboBox.getValue());
		salarie.setSite(siteComboBox.getValue());

		saveClicked = true;
		dialogStage.close();
	}

	public Salarie getSalarie() {
		return salarie;
	}
}