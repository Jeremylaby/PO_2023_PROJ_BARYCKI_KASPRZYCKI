package agh.ics.oop.presenter;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Map;

public class SimulationPresenter implements MapChangeListener {
    private final static double CELL_WIDTH = 50;
    private final static double CELL_HEIGHT = 50;

    @FXML
    private Label infoLabel;

    @FXML
    private GridPane mapGrid;

    private WorldMap map;

    public void setWorldMap(WorldMap map) {
        this.map = map;
    }

    public void drawMap(WorldMap worldMap) {
        clearGrid();
        Boundary boundary = worldMap.getCurrentBounds();
        setGrid(boundary);
        drawWorldElements(worldMap, boundary);
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private void setGrid(Boundary boundary) {
        Vector2d lhvector = boundary.rightUpper().subtract(boundary.leftLower());
        int x = boundary.leftLower().getX();
        int y = boundary.rightUpper().getY();

        for (int i = 0; i <= lhvector.getX() + 1; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int i = 0; i <= lhvector.getY() + 1; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i = 1; i <= lhvector.getX() + 1; i++) {
            Label label = new Label("" + x);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, i, 0);
            x += 1;
        }
        for (int i = 1; i <= lhvector.getY() + 1; i++) {
            Label label = new Label("" + y);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label, 0, i);
            y -= 1;
        }
        Label label = new Label("x/y");
        GridPane.setHalignment(label, HPos.CENTER);
        mapGrid.add(label, 0, 0);
    }

    private void drawWorldElements(WorldMap worldMap, Boundary boundary) {
        int x = boundary.leftLower().getX();
        int y = boundary.leftLower().getY();
        Vector2d lhvector = boundary.rightUpper().subtract(boundary.leftLower());
        int height = lhvector.getY();
        Map<Vector2d, WorldElement> elements = worldMap.getElements();
        elements.forEach((key, value) -> {
            if (value instanceof Animal) {
//                Image image = new Image("/images/pig.png");
//                ImageView imageView = new ImageView(image);
//                imageView.setPreserveRatio(true);
//                imageView.setFitWidth(CELL_WIDTH);
//                imageView.setFitHeight(CELL_HEIGHT);
                Label label = new Label(value.toString());
                label.setPrefWidth(CELL_WIDTH);
                label.setPrefHeight(CELL_HEIGHT);

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll( label);
                label.setAlignment(Pos.CENTER);
                mapGrid.add(stackPane, key.getX() - x + 1, height - (key.getY() - y) + 1);
            } else {
//                Image image = new Image("/images/grass.png");
//                ImageView imageView = new ImageView(image);
//                imageView.setPreserveRatio(true);
//                imageView.setFitWidth(CELL_WIDTH);
//                imageView.setFitHeight(CELL_HEIGHT);
                Label label = new Label(value.toString());
                label.setPrefWidth(CELL_WIDTH);
                label.setPrefHeight(CELL_HEIGHT);
                mapGrid.add(label, key.getX() - x + 1, height - (key.getY() - y) + 1);
            }

        });
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(worldMap);
            infoLabel.setText(message);
        });

    }
}
