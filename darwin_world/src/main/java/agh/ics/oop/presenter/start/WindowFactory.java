package agh.ics.oop.presenter.start;

import agh.ics.oop.presenter.simulation.SimulationPresenter;
import agh.ics.oop.simulation.Simulation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowFactory {
    public static final String SIMULATION_WINDOW_FXML = "simulationWindow.fxml";

    public static SimulationPresenter createSimulationWindow(Simulation simulation) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(WindowFactory.class.getClassLoader().getResource(SIMULATION_WINDOW_FXML));

        configureStage(loader, stage, simulation);
        stage.show();

        return loader.getController();
    }

    private static void configureStage(FXMLLoader loader, Stage stage, Simulation simulation) throws IOException {
        BorderPane viewRoot = loader.load();
        Scene scene = new Scene(viewRoot);
        stage.setScene(scene);
        stage.setTitle("Symulacja");
        stage.setOnCloseRequest(event -> simulation.stop());
    }
}
