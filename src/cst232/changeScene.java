package cst232;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class changeScene {


    changeScene(AnchorPane pane1, StackPane stack1, String fxmlFile, int way) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = pane1.getScene();
            if(way == 1)root.translateXProperty().set(scene.getWidth());
            else root.translateXProperty().set(-scene.getWidth());
            stack1.getChildren().add(root);


            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(t -> {
                stack1.getChildren().remove(pane1);
            });
            timeline.play();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("FXML missing");
            alert.setContentText("File: MenuSelection.fxml");
        }

    }

    changeScene(AnchorPane pane1, StackPane stack1, String fxmlFile, int way, int page) {

        try {


            FXMLLoader loader= new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = pane1.getScene();

            if(page == 1){
            cst232.Menu controller = loader.getController();
            controller.initializeArray();}

            else if(page == 2){
                cst232.FixedSimulation controller = loader.getController();
                ActionEvent event2 = new ActionEvent();
                scene.setOnKeyPressed(event -> {
                    controller.addJob(event2);
                });
            }
            else if(page == 3){
                cst232.DynamicSimulation controller = loader.getController();
                ActionEvent event2 = new ActionEvent();
                scene.setOnKeyPressed(event -> {
                    controller.addJob(event2);
                });
            }


            if(way == 1)root.translateXProperty().set(scene.getWidth());
            else root.translateXProperty().set(-scene.getWidth());
            stack1.getChildren().add(root);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(t -> {
                stack1.getChildren().remove(pane1);
            });
            timeline.play();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("FXML missing");
            alert.setContentText("File: MenuSelection.fxml");
        }
    }
}
