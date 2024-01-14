package agh.ics.oop.presenter;

import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationEngine;
import agh.ics.oop.model.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPresenter {
    private SimulationEngine engine;

    @FXML
    private void initialize() {
        engine = new SimulationEngine();
    }

    public void onSimulationStartClicked() {
        try {
            Simulation simulation = new Simulation(getConfiguration());
            startSimulation(simulation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Configuration getConfiguration() {
        return new Configuration(
                10,
                10,
                5,
                10,
                2,
                false,
                1,
                10,
                20,
                10,
                0,
                0,
                10,
                false
        );
    }

    private void startSimulation(Simulation simulation) throws Exception {
        SimulationPresenter presenter = createSimulationWindow(simulation);
        presenter.setWorldMap(simulation.getWorldMap());
        simulation.addListener(presenter);
        engine.runSimulation(simulation);
    }

    private SimulationPresenter createSimulationWindow(Simulation simulation) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        configureStage(viewRoot, stage);
        stage.setOnCloseRequest(event -> simulation.stop());
        stage.show();
        return presenter;
    }

    private static void configureStage(BorderPane viewRoot, Stage stage) {
//        Image icon = new Image("/images/pig2.png");
//        newStage.getIcons().add(icon);
        Scene scene = new Scene(viewRoot);
        stage.setScene(scene);
        stage.setTitle("Simulation Window");
    }

    public void shutdown() {
        try {
            engine.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
