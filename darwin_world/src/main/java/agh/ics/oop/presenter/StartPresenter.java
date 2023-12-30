package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.Vector2d;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartPresenter {

    public void onSimulationStartClicked() {
        try {
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(6, 3));

            SimulationPresenter presenter = createNewSimulationWindow();
            GrassField map = new GrassField(10);
            Simulation simulation = new Simulation(positions, map);

            presenter.setWorldMap(map);
            map.addObserver(presenter);

            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.runAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SimulationPresenter createNewSimulationWindow() throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter newPresenter = loader.getController();
        configureStage(viewRoot, newStage);
        newStage.show();
        return newPresenter;
    }

    private static void configureStage(BorderPane viewRoot, Stage newStage) {
//        Image icon = new Image("/images/pig2.png");
//        newStage.getIcons().add(icon);
        Scene scene = new Scene(viewRoot);
        newStage.setScene(scene);
        newStage.setTitle("Simulation Window");
    }
}
