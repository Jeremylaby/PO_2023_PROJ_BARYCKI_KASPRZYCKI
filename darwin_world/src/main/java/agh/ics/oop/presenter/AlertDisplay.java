package agh.ics.oop.presenter;

import javafx.scene.control.Alert;

import java.nio.charset.StandardCharsets;

public class AlertDisplay {

    public static void showSuccessAlert(String message) {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Informacja");
        informationAlert.setHeaderText("OK");
        informationAlert.setContentText(new String(message.getBytes(), StandardCharsets.UTF_8));
        informationAlert.showAndWait();
    }

    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(new String(title.getBytes(), StandardCharsets.UTF_8));
        alert.setContentText(new String(message.getBytes(), StandardCharsets.UTF_8));
        alert.showAndWait();
    }
}
