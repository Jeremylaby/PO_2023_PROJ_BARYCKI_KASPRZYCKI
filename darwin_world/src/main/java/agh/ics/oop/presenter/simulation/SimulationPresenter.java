package agh.ics.oop.presenter.simulation;

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
    private Label numberOfAnimals;
    @FXML
    private Label numberOfPlants;
    @FXML
    private Label numberOfEmptyPositions;
    @FXML
    private Label avgAnimalEnergy;
    @FXML
    private Label avgDaySurvived;
    @FXML
    private Label avgKidsNumber;
    @FXML
    private Label mostPopularGenes;

    @FXML
    private Button pauseResumeButton;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label selectedAnimalLabel;
    @FXML
    private Button unfollowAnimalButton;
    @FXML
    private Label dayOfSimulation;

    private Simulation simulation;
    private WorldMap map;
    private double cellSize = 0;
    private WorldElement selectedElement;

    @Override
    public void mapChanged(WorldMap worldMap) {
        Platform.runLater(this::drawMap);
    }

    public void onSimulationPauseClicked() {
        if (simulation.toggle()) {
            pauseResumeButton.setText("wznów");
        } else {
            pauseResumeButton.setText("zatrzymaj");
        }
    }

    public void drawMap() {
        clearGrid();
        drawPreferredPlantPositions();
        drawWorldElements();
        displayFollowedElement();
        displayStatistics(simulation.getStatistics());
    }

    private void clearGrid() {
        mapGrid.getChildren().clear();
    }

    private void displayStatistics(Statistics stats) {
        dayOfSimulation.setText("Dzień symulacji: " + simulation.getDayOfSimulation());
        numberOfAnimals.setText("" + stats.getNumOfAnimals());
        numberOfPlants.setText("" + stats.getNumOfPlants());
        numberOfEmptyPositions.setText("" + stats.getNumOfEmptyPos());
        avgAnimalEnergy.setText("%.2f".formatted(stats.getAvgEnergy()));
        avgDaySurvived.setText("%.2f".formatted(stats.getAvgDaySurvived()));
        avgKidsNumber.setText("" + stats.getAvgKidsNumber());
        mostPopularGenes.setText(stats.getMostPopularGenesForDisplay());
    }

    private void drawPreferredPlantPositions() {
        synchronized (map.getPreferredPlantPositions()) {
            map.getPreferredPlantPositions().forEach(element ->
                    mapGrid.add(
                            createGridCell(element),
                            element.getPosition().x(),
                            element.getPosition().y()
                    ));
        }
    }

    private void displayFollowedElement() {
        if (selectedElement != null) {
            selectedAnimalLabel.setText(selectedElement.toString());
        }
    }

    private void drawWorldElements() {
        map.getElements().forEach(element ->
                mapGrid.add(
                        createGridCell(element),
                        element.getPosition().x(),
                        element.getPosition().y()
                ));
    }

    private Node createGridCell(WorldElement element) {
        Node elementBox = new WorldElementBox(
                element,
                cellSize,
                element.equals(selectedElement),
                element.hasDominatingGenes(simulation.getMostPopularGenes())
        ).getFxElement();

        if (element.isSelectable()) {
            elementBox.setOnMouseClicked(event -> {
                selectedElement = element.equals(selectedElement) ? null : element;
                changeAnimalInfoVisibility(selectedElement != null);
                mapChanged(map);
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
        changeAnimalInfoVisibility(false);
        mapChanged(map);
    }

    private void changeAnimalInfoVisibility(boolean isVisible) {
        unfollowAnimalButton.setVisible(isVisible);
        if (!isVisible) {
            selectedAnimalLabel.setText("");
        }
    }
}
