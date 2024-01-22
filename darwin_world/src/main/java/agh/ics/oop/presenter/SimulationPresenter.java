package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import agh.ics.oop.model.elements.WorldElement;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class SimulationPresenter implements MapChangeListener {
    private final static double GRID_SIZE = 600;

    @FXML
    private Button pauseResumeButton;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label statistics;
    @FXML
    private Label animalLabel;
    @FXML
    private Button animalButton;
    @FXML
    private Label dayOfSimulation;
    private Simulation simulation;
    private WorldMap map;
    private double cellSize = 0;
    private WorldElement selectedElement;

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
        statistics.setText(simulation.getStatistics().toString());
        drawFollowedElement();
        displayDayOfSimulation();
        drawWorldElements();
    }

    private void displayDayOfSimulation() {
        dayOfSimulation.setText("DAY: " + String.valueOf(simulation.getDayOfSimulation()));
    }

    private void drawFollowedElement() {
        if (selectedElement != null) {
            animalLabel.setText(selectedElement.toString());
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().clear();
    }

    private void drawWorldElements() {
        map.getElements().forEach((element) -> {
            mapGrid.add(
                    createGridCell(element),
                    element.getPosition().x(),
                    element.getPosition().y()
            );
        });
    }

    private Node createGridCell(WorldElement element) {
        VBox elementBox = new WorldElementBox(element, cellSize, element.equals(selectedElement)).getFxElement();

        if (element.isSelectable()) {
            elementBox.setOnMouseClicked(event -> {
                selectedElement = element.equals(selectedElement) ? null : element;

                if (selectedElement == null) {
                    changeVisible(false);
                } else {
                    changeVisible(true);
                }
                mapChanged(map, "");
            });
        }

        return elementBox;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.map = simulation.getWorldMap();
        createGrid();
    }

    private void createGrid() {
        cellSize = GRID_SIZE / Math.max(map.getHeight(), map.getWidth());

        for (int i = 0; i < map.getWidth(); i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }

        for (int i = 0; i < map.getHeight(); i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }

    public void stopFollow() {
        selectedElement = null;
        changeVisible(false);

    }

    private void changeVisible(boolean flag) {
        animalLabel.setVisible(flag);
        animalButton.setVisible(flag);
        if (!flag) {
            animalLabel.setText("");
        }
    }
}
