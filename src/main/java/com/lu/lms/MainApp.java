package com.lu.lms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Lucknow University LMS");
primaryStage.setResizable(true);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(700);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        scene.getStylesheets().add("/application.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void switchScene(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.getStylesheets().add("/application.css");
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
