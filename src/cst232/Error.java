package cst232;

import javafx.scene.control.Alert;

public class Error {
    public Error(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("FXML missing");
        alert.setContentText("File: MenuSelection.fxml");
    }
}
