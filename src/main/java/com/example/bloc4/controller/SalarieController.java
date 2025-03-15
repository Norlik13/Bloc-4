package com.example.bloc4.controller;

import com.example.bloc4.model.Department;
import com.example.bloc4.model.Salarie;
import com.example.bloc4.model.Site;
import com.example.bloc4.model.User;
import com.example.bloc4.service.DepartmentService;
import com.example.bloc4.service.SalarieService;
import com.example.bloc4.service.SiteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SalarieController {

	@FXML
	private TableView<Salarie> salarieTable;
	@FXML
	private TableColumn<Salarie, String> nomColumn;
	@FXML
	private TableColumn<Salarie, String> prenomColumn;
	@FXML
	private TableColumn<Salarie, String> telephoneFixeColumn;
	@FXML
	private TableColumn<Salarie, String> telephonePortableColumn;
	@FXML
	private TableColumn<Salarie, String> emailColumn;
	@FXML
	private TableColumn<Salarie, String> departmentColumn;
	@FXML
	private TableColumn<Salarie, String> siteColumn;
	@FXML
	private TextField nameFilterField;
	@FXML
	private TextField prenomFilterField;
	@FXML
	private ComboBox<Site> siteFilterComboBox;
	@FXML
	private ComboBox<Department> departmentFilterComboBox;
	@FXML
	private Button ajouterSalarieButton;
	@FXML
	private Button modifierSalarieButton;
	@FXML
	private Button supprimerSalarieButton;
	@FXML
	private Button addDepartmentButton;
	@FXML
	private Button deleteDepartmentButton;
	@FXML
	private Button addSiteButton;
	@FXML
	private Button deleteSiteButton;

	private final SalarieService salarieService = new SalarieService();
	private final DepartmentService departmentService = new DepartmentService();
	private final SiteService siteService = new SiteService();
	private FilteredList<Salarie> filteredData;
	User currentUser = new User(User.Role.INVITED);
	private final String adminPassword = "admin";

	private final KeyCombination ctrlACombination = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);

	@FXML
	private void initialize() {
		salarieTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
		prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		telephoneFixeColumn.setCellValueFactory(new PropertyValueFactory<>("telephoneFixe"));
		telephonePortableColumn.setCellValueFactory(new PropertyValueFactory<>("telephonePortable"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getName()));
		siteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSite().getName()));

		try {
			List<Salarie> salaries = salarieService.fetchSalariesFromAPI();
			filteredData = new FilteredList<>(FXCollections.observableArrayList(salaries), p -> true);
			salarieTable.setItems(filteredData);

			List<Site> sites = new SiteService().fetchSitesFromAPI();
			siteFilterComboBox.getItems().addAll(sites);

			List<Department> departments = new DepartmentService().fetchDepartmentsFromAPI();
			departmentFilterComboBox.getItems().addAll(departments);
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to fetch data from API");
		}
	}

	@FXML
	private void handleSearch() {
		String nameFilter = nameFilterField.getText().toLowerCase();
		String prenomFilter = prenomFilterField.getText().toLowerCase();
		Site selectedSite = siteFilterComboBox.getValue();
		Department selectedDepartment = departmentFilterComboBox.getValue();

		filteredData.setPredicate(salarie -> {
			boolean matchesName = nameFilter.isEmpty() || salarie.getNom().toLowerCase().contains(nameFilter);
			boolean matchesPrenom = prenomFilter.isEmpty() || salarie.getPrenom().toLowerCase().contains(prenomFilter);
			boolean matchesSite = selectedSite == null || salarie.getSite().equals(selectedSite);
			boolean matchesDepartment = selectedDepartment == null || salarie.getDepartment().equals(selectedDepartment);
			return matchesName && matchesPrenom && matchesSite && matchesDepartment;
		});

		nameFilterField.clear();
		prenomFilterField.clear();
		siteFilterComboBox.setValue(null);
		siteFilterComboBox.setPromptText("Selectionnez un site");
		departmentFilterComboBox.setValue(null);
		departmentFilterComboBox.setPromptText("Selectionnez un departement");
	}

	@FXML
	private void handleResetFilter() {
		nameFilterField.clear();
		prenomFilterField.clear();
		siteFilterComboBox.setValue(null);
		siteFilterComboBox.setPromptText("Selectionnez un site");
		departmentFilterComboBox.setValue(null);
		departmentFilterComboBox.setPromptText("Selectionnez un departement");
		filteredData.setPredicate(salarie -> true);
	}

	@FXML
	public void handleKeyPress(KeyEvent event) {
		if (ctrlACombination.match(event)) {
			if (currentUser.getRole() == User.Role.ADMIN) {
				currentUser.setRole(User.Role.INVITED);
			} else {
				showAdminLoginDialog();
			}
			updateButtonVisibility();
		}
	}

	private void showAdminLoginDialog() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Admin Login");
		dialog.setHeaderText("Enter Admin Password");

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Password");

		GridPane grid = new GridPane();
		grid.add(new Label("Password:"), 0, 0);
		grid.add(passwordField, 1, 0);
		GridPane.setHgrow(passwordField, Priority.ALWAYS);

		Node loginButton = dialog.getDialogPane().lookupButton(dialog.getDialogPane().getButtonTypes().getFirst());
		loginButton.disableProperty().bind(passwordField.textProperty().isEmpty());

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == dialog.getDialogPane().getButtonTypes().getFirst()) {
				return passwordField.getText();
			}
			return null;
		});

		dialog.showAndWait().ifPresent(password -> {
			if (password.equals(adminPassword)) {
				currentUser.setRole(User.Role.ADMIN);
				updateButtonVisibility();
			} else {
				showAlert("Error", "Incorrect password");
			}
		});
	}

	private void updateButtonVisibility() {
		boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
		ajouterSalarieButton.setVisible(isAdmin);
		modifierSalarieButton.setVisible(isAdmin);
		supprimerSalarieButton.setVisible(isAdmin);
		addDepartmentButton.setVisible(isAdmin);
		deleteDepartmentButton.setVisible(isAdmin);
		addSiteButton.setVisible(isAdmin);
		deleteSiteButton.setVisible(isAdmin);
	}

	@FXML
	private void handleAddSalarie() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bloc4/salarie-add-view.fxml"));
			Parent page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Ajout Salarie");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(salarieTable.getScene().getWindow());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			AddSalarieController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.showAndWait();

			if (controller.isSaveClicked()) {
				Salarie newSalarie = controller.getSalarie();
				salarieService.postSalarieToAPI(newSalarie);
				ObservableList<Salarie> salarieList = FXCollections.observableArrayList(salarieTable.getItems());
				salarieList.add(newSalarie);
				salarieTable.setItems(salarieList);
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to open add dialog");
		}
	}

	@FXML
	private void handleUpdateSalarie() {
		Salarie selectedSalarie = salarieTable.getSelectionModel().getSelectedItem();
		if (selectedSalarie != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bloc4/salarie-modify-view.fxml"));
				Parent page = loader.load();
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Modification Salarie");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(salarieTable.getScene().getWindow());
				Scene scene = new Scene(page);
				dialogStage.setScene(scene);


				ModifySalarieController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				controller.setSalarie(selectedSalarie);

				dialogStage.showAndWait();

				if (controller.isSaveClicked()) {
					salarieTable.refresh();
				}
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to open modify dialog");
			}
		} else {
			showAlert("No Selection", "No salarie selected for update");
		}
	}

	@FXML
	private void handleDeleteSalarie() {
		Salarie selectedSalarie = salarieTable.getSelectionModel().getSelectedItem();
		if (selectedSalarie != null) {
			try {
				salarieService.deleteSalarieFromAPI(selectedSalarie.getId());
				salarieTable.getItems().remove(selectedSalarie);
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to delete salarie");
			}
		} else {
			showAlert("No Selection", "No salarie selected for deletion");
		}
	}

	@FXML
	private void handleAddDepartment() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Department");
		dialog.setHeaderText("Enter Department Name");
		dialog.setContentText("Name:");

		dialog.showAndWait().ifPresent(name -> {
			Department newDepartment = new Department();
			newDepartment.setName(name);
			try {
				departmentService.postDepartmentToAPI(newDepartment);
				departmentFilterComboBox.getItems().add(newDepartment);
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to add department");
			}
		});
	}

	@FXML
	private void handleDeleteDepartment() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bloc4/selection-dialog.fxml"));
			Parent page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Select Department to Delete");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(salarieTable.getScene().getWindow());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			SelectionDialogController<Department> controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setItems(departmentFilterComboBox.getItems());

			dialogStage.showAndWait();

			if (controller.isDeleteClicked()) {
				Department selectedDepartment = controller.getSelectedItem();
				if (selectedDepartment != null) {
					// Check if any Salarie refers to this Department
					boolean hasSalaries = filteredData.stream()
							.anyMatch(salarie -> salarie.getDepartment().equals(selectedDepartment));
					if (hasSalaries) {
						showAlert("Error", "Cannot delete department. There are salaries referring to it.");
					} else {
						departmentService.deleteDepartmentFromAPI(selectedDepartment.getId());
						departmentFilterComboBox.getItems().remove(selectedDepartment);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to open selection dialog");
		}
	}

	@FXML
	private void handleAddSite() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Site");
		dialog.setHeaderText("Enter Site Name");
		dialog.setContentText("Name:");

		dialog.showAndWait().ifPresent(name -> {
			Site newSite = new Site();
			newSite.setName(name);
			try {
				siteService.postSiteToAPI(newSite);
				siteFilterComboBox.getItems().add(newSite);
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to add site");
			}
		});
	}

	@FXML
	private void handleDeleteSite() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bloc4/selection-dialog.fxml"));
			Parent page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Select Site to Delete");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(salarieTable.getScene().getWindow());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			SelectionDialogController<Site> controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setItems(siteFilterComboBox.getItems());

			dialogStage.showAndWait();

			if (controller.isDeleteClicked()) {
				Site selectedSite = controller.getSelectedItem();
				if (selectedSite != null) {
					// Check if any Salarie refers to this Site
					boolean hasSalaries = filteredData.stream()
							.anyMatch(salarie -> salarie.getSite().equals(selectedSite));
					if (hasSalaries) {
						showAlert("Error", "Cannot delete site. There are salaries referring to it.");
					} else {
						siteService.deleteSiteFromAPI(selectedSite.getId());
						siteFilterComboBox.getItems().remove(selectedSite);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to open selection dialog");
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