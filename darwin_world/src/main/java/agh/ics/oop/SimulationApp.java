package agh.ics.oop;

import agh.ics.oop.presenter.StartPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("startWindow.fxml"));

        BorderPane viewRoot = loader.load();
        StartPresenter startPresenter = loader.getController();

        configureStage(primaryStage, viewRoot);
        primaryStage.setOnCloseRequest((event) -> startPresenter.shutdown());
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
//        Image icon = new Image("/images/pig.png");
//        primaryStage.getIcons().add(icon);
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
