package com.lu.lms.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.lu.lms.MainApp;
import com.lu.lms.model.UserSession;
import com.lu.lms.service.AlertService;
import com.lu.lms.service.IssueService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

public class LibrarianDashboardController implements Initializable {
    @FXML private ListView<String> alertsList;
    @FXML private ChoiceBox<String> studentChoice;
    @FXML private ListView<String> studentIssuesList;
    @FXML private Button sendAlertBtn;
    @FXML private Button logoutBtn;

    private AlertService alertService = new AlertService();
    private IssueService issueService = new IssueService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAlerts();
        loadStudents();
    }

    private void loadAlerts() {
        alertsList.setItems(FXCollections.observableArrayList(alertService.getAlerts()));
    }

    private void loadStudents() {
        // Hardcoded for prototype
        studentChoice.setItems(FXCollections.observableArrayList("John Doe (ID:2)"));
    }

    @FXML
    private void handleStudentSelect(ActionEvent event) {
        String selected = studentChoice.getValue();
        if (selected == null) return;
        try {
            String idStr = selected.substring(selected.lastIndexOf(':') + 1, selected.lastIndexOf(')'));
            long studentId = Long.parseLong(idStr);
            loadIssuesForStudent(studentId);
        } catch (Exception e) {
            // ignore
        }
    }

    private void loadIssuesForStudent(long studentId) {
        studentIssuesList.setItems(FXCollections.observableArrayList(
            issueService.getIssuesByStudent(studentId).stream()
                .map(i -> "ID:" + i.getId() + " BookID:" + i.getBookId() + " Due:" + i.getDueDate() + 
                          (i.getReturnDate() == null ? " Active" : " Returned Fine: Rs" + i.getFine()))
                .collect(Collectors.toList())
        ));
    }

    @FXML
    private void handleSendAlerts(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Alerts sent to all near-expiry students!");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.logout();
        try {
            MainApp.switchScene("/view/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Librarian Dashboard");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
