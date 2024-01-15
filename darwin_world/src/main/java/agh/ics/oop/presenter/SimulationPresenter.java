package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class SimulationPresenter implements MapChangeListener {
    private final static double GRID_WIDTH = 500;
    private final static double GRID_HEIGHT = 500;

    @FXML
    private Label infoLabel;

    @FXML
    private GridPane mapGrid;

    private WorldMap map;

    private static Label createLabel(String text) {
        Label label = new Label(text);
        GridPane.setHalignment(label, HPos.CENTER);
        return label;
    }

    private static Node createGridCell(WorldElement element) {
        Label label = createLabel(element.toString());
        return label;
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            infoLabel.setText(message);
        });
    }

    public void drawMap() {
        clearGrid();
        createGrid();
        drawWorldElements();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
    }

    private void createGrid() {
        int height = map.getHeight();

        mapGrid.add(createLabel("y/x"), 0, height);

        for (int x = 0; x < map.getWidth(); x++) {
            mapGrid.add(createLabel("%d".formatted(x)), x + 1, height);
        }

        for (int y = 0; y < map.getHeight(); y++) {
            mapGrid.add(createLabel("%d".formatted(y)), 0, height - y - 1);
        }
    }

    private void drawWorldElements() {
        map.getElements().forEach((element) -> {
            mapGrid.add(
                    createGridCell(element),
                    element.getPosition().getX() + 1,
                    map.getHeight() - element.getPosition().getY() - 1
            );
        });
    }

    public void setWorldMap(WorldMap map) {
        this.map = map;

        for (int i = 0; i < map.getWidth() + 1; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(GRID_WIDTH / (map.getWidth() + 1)));
        }
        for (int i = 0; i < map.getHeight() + 1; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(GRID_HEIGHT / (map.getHeight() + 1)));
        }
    }
}
