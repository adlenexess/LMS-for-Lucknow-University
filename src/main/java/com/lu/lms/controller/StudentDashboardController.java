package com.lu.lms.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.lu.lms.MainApp;
import com.lu.lms.model.UserSession;
import com.lu.lms.service.BookService;
import com.lu.lms.service.AuthService;
import com.lu.lms.service.IssueService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class StudentDashboardController implements Initializable {
    @FXML private ListView<String> booksList;
    @FXML private ListView<String> issuesList;
    @FXML private Button issueBtn, returnBtn, logoutBtn;

    private BookService bookService = new BookService();
    private IssueService issueService = new IssueService();
    private AuthService authService = new AuthService();
    private long studentId = authService.getStudentId(UserSession.getCurrentUserId());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBooks();
        loadIssues();
    }

    private void loadBooks() {
        booksList.setItems(FXCollections.observableArrayList(
            bookService.getAllBooks().stream()
                .filter(b -> b.isAvailable())
                .map(b -> b.getTitle() + " (ID:" + b.getId() + ")")
                .collect(Collectors.toList())
        ));
    }

    private void loadIssues() {
        issuesList.setItems(FXCollections.observableArrayList(
            issueService.getIssuesByStudent(studentId).stream()
                .map(i -> "ID:" + i.getId() + " BookID:" + i.getBookId() + " Due:" + i.getDueDate() + 
                          (i.getReturnDate() == null ? " Active" : " Returned Fine: Rs" + i.getFine()))
                .collect(Collectors.toList())
        ));
    }

    @FXML
    private void handleIssue(ActionEvent event) {
        String selected = booksList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select a book");
            return;
        }
        try {
            String idStr = selected.substring(selected.indexOf("ID:") + 3, selected.indexOf(')', selected.indexOf("ID:")));
            long bookId = Long.parseLong(idStr);
            issueService.issueBook(studentId, bookId);
            loadBooks();
            loadIssues();
            showAlert(Alert.AlertType.INFORMATION, "Book issued successfully");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error issuing book");
        }
    }

    @FXML
    private void handleReturn(ActionEvent event) {
        String selected = issuesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select an issue");
            return;
        }
        try {
            String idStr = selected.substring(3, selected.indexOf(' ', 3)); // ID:3 -> 3
            long issueId = Long.parseLong(idStr);
            issueService.returnBook(issueId);
            loadBooks();
            loadIssues();
            showAlert(Alert.AlertType.INFORMATION, "Book returned");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error returning book");
        }
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
        alert.setTitle("Student Dashboard");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
