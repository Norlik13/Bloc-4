package com.example.bloc4.controller;

import com.example.bloc4.model.Department;
import com.example.bloc4.service.DepartmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class DepartmentController {

	@FXML
	private TableView<Department> departmentTable;
	@FXML
	private TextField nameField;

	private DepartmentService departmentService = new DepartmentService();

	@FXML
	private void initialize() {
		try {
			List<Department> departments = departmentService.fetchDepartmentsFromAPI();
			departmentTable.getItems().addAll(departments);
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to fetch data from API");
		}
	}

	@FXML
	private void handleAddDepartment() {
		Department department = new Department();
		department.setName(nameField.getText());
		try {
			departmentService.postDepartmentToAPI(department);
			departmentTable.getItems().add(department);
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to add department");
		}
	}

	@FXML
	private void handleUpdateDepartment() {
		Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
		if (selectedDepartment != null) {
			selectedDepartment.setName(nameField.getText());
			try {
				departmentService.updateDepartmentInAPI(selectedDepartment);
				departmentTable.refresh();
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to update department");
			}
		} else {
			showAlert("No Selection", "No department selected for update");
		}
	}

	@FXML
	private void handleDeleteDepartment() {
		Department selectedDepartment = departmentTable.getSelectionModel().getSelectedItem();
		if (selectedDepartment != null) {
			try {
				departmentService.deleteDepartmentFromAPI(selectedDepartment.getId());
				departmentTable.getItems().remove(selectedDepartment);
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to delete department");
			}
		} else {
			showAlert("No Selection", "No department selected for deletion");
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