package agh.ics.oop.presenter;

import javafx.scene.control.Alert;

public class AlertDisplay {

    public static void showSuccessAlert(String message) {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Informacja");
        informationAlert.setHeaderText("OK");
        informationAlert.setContentText(message);
        informationAlert.showAndWait();
    }

    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
