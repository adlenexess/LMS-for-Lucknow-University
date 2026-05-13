package com.lu.lms.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.lu.lms.MainApp;
import com.lu.lms.model.Librarian;
import com.lu.lms.model.Role;
import com.lu.lms.model.Student;
import com.lu.lms.service.AuthService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Role> roleChoice;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private Button registerBtn;
    @FXML private Hyperlink loginLink;

    private AuthService authService = new AuthService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleChoice.getItems().addAll(Role.STUDENT, Role.LIBRARIAN);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        Role role = roleChoice.getValue();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill required fields");
            return;
        }

        boolean success = false;
        if (role == Role.STUDENT) {
            if (email.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Email required for student");
                return;
            }
            Student student = new Student(username, password, name, email);
            success = authService.registerStudent(student);
        } else {
            Librarian librarian = new Librarian(username, password, name);
            success = authService.registerLibrarian(librarian);
        }

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Account created! Please login.");
            try {
                MainApp.switchScene("/view/Login.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration failed. Username may exist.");
        }
    }

    @FXML
    private void handleLoginLink(ActionEvent event) {
        try {
            MainApp.switchScene("/view/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("LMS Register");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

