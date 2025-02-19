package com.example.bloc4.controller;

import com.example.bloc4.model.Site;
import com.example.bloc4.service.SiteService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class SiteController {

	@FXML
	private TableView<Site> siteTable;
	@FXML
	private TextField villeField;

	private SiteService siteService = new SiteService();

	@FXML
	private void initialize() {
		try {
			List<Site> sites = siteService.fetchSitesFromAPI();
			siteTable.getItems().addAll(sites);
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to fetch data from API");
		}
	}

	@FXML
	private void handleAddSite() {
		Site site = new Site();
		site.setName(villeField.getText());
		try {
			siteService.postSiteToAPI(site);
			siteTable.getItems().add(site);
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to add site");
		}
	}

	@FXML
	private void handleUpdateSite() {
		Site selectedSite = siteTable.getSelectionModel().getSelectedItem();
		if (selectedSite != null) {
			selectedSite.setName(villeField.getText());
			try {
				siteService.updateSiteInAPI(selectedSite);
				siteTable.refresh();
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to update site");
			}
		} else {
			showAlert("No Selection", "No site selected for update");
		}
	}

	@FXML
	private void handleDeleteSite() {
		Site selectedSite = siteTable.getSelectionModel().getSelectedItem();
		if (selectedSite != null) {
			try {
				siteService.deleteSiteFromAPI(selectedSite.getId());
				siteTable.getItems().remove(selectedSite);
			} catch (IOException e) {
				e.printStackTrace();
				showAlert("Error", "Failed to delete site");
			}
		} else {
			showAlert("No Selection", "No site selected for deletion");
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