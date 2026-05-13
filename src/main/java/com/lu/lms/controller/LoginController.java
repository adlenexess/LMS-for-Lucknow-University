package com.lu.lms.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.lu.lms.MainApp;
import com.lu.lms.model.Role;
import com.lu.lms.model.UserSession;
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

public class LoginController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<Role> roleChoice;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private Hyperlink registerLink; // To switch to register if added

    private AuthService authService = new AuthService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleChoice.getItems().addAll(Role.STUDENT, Role.LIBRARIAN);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Role role = roleChoice.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill all fields");
            return;
        }

        Long userId = authService.login(username, password, role);
        if (userId != null) {
            // Store userId in some static or pass to next controller (simple: use static var)
            UserSession.setCurrentUserId(userId);
            UserSession.setCurrentRole(role);
            try {
                if (role == Role.STUDENT) {
                    MainApp.switchScene("/view/StudentDashboard.fxml");
                } else {
                    MainApp.switchScene("/view/LibrarianDashboard.fxml");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid credentials");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            MainApp.switchScene("/view/Register.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("LMS");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
