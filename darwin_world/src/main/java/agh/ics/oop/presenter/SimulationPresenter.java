package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import agh.ics.oop.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class SimulationPresenter implements MapChangeListener {
    private final static double GRID_SIZE = 600;

    @FXML
    private Button pauseResumeButton;
    @FXML
    private GridPane mapGrid;

    private Simulation simulation;
    private WorldMap map;
    private double cellSize = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(this::drawMap);
    }

    public void onSimulationPauseClicked() {
        if (simulation.toggle()) {
            pauseResumeButton.setText("resume");
        } else {
            pauseResumeButton.setText("pause");
        }
    }

    public void drawMap() {
        clearGrid();
        drawWorldElements();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
    }

    private void drawWorldElements() {
        map.getElements().forEach((element) -> {
            mapGrid.add(
                    createGridCell(element),
                    element.getPosition().x() + 1,
                    map.getHeight() - element.getPosition().y() - 1
            );
        });
    }

    private Node createGridCell(WorldElement element) {
        WorldElementBox elementBox = new WorldElementBox(element, cellSize, cellSize);
        return elementBox.getFxElement();
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.map = simulation.getWorldMap();
        createGrid();
    }

    private void createGrid() {
        cellSize = GRID_SIZE / Math.max(map.getHeight(), map.getWidth());

        for (int i = 0; i < map.getWidth() + 1; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }

        for (int i = 0; i < map.getHeight() + 1; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }
}
