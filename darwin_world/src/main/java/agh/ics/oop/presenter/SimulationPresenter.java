package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulationPresenter implements MapChangeListener {
    @FXML
    private Label infoLabel;
    @FXML
    private TextField movesTextField;
    @FXML
    private GridPane mapGrid;
    private  WorldMap map;
    private final  static double CELL_WIDTH=50;
    private final  static double CELL_HEIGHT=50;
    public void setWorldMap(WorldMap map){
        this.map=map;
    }
    private void createGrid(Boundary boundary){
        Vector2d lhvector=boundary.rightUpper().subtract(boundary.leftLower());
        int x = boundary.leftLower().getX();
        int y = boundary.rightUpper().getY();
        for(int i=0;i<=lhvector.getX()+1;i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }
        for (int i=0;i<=lhvector.getY()+1;i++){
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        }
        for (int i=1;i<=lhvector.getX()+1;i++){
            Label label = new Label(""+x);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label,i,0);
            x+=1;
        }
        for (int i=1;i<=lhvector.getY()+1;i++){
            Label label = new Label(""+y);
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.add(label,0,i);
            y-=1;
        }
        Label label = new Label("x/y");
        GridPane.setHalignment(label, HPos.CENTER);
        mapGrid.add(label, 0, 0);
    }
    public void drawMap(WorldMap worldMap){
        clearGrid();
        Boundary boundary =  worldMap.getCurrentBounds();
        createGrid(boundary);
        drawWorldElements(worldMap,boundary);
        mapGrid.setAlignment(Pos.CENTER);

    }
    private void drawWorldElements(WorldMap worldMap,Boundary boundary){
        int x = boundary.leftLower().getX();
        int y = boundary.leftLower().getY();
        Vector2d lhvector=boundary.rightUpper().subtract(boundary.leftLower());
        int height=lhvector.getY();
        Map<Vector2d,WorldElement> elements= worldMap.getElements();
        elements.forEach((key,value)->{
            if(value instanceof Animal){
                Image image = new Image("/images/pig.png");
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(CELL_WIDTH);
                imageView.setFitHeight(CELL_HEIGHT);
                Label label = new Label(value.toString());
                label.setPrefWidth(CELL_WIDTH);
                label.setPrefHeight(CELL_HEIGHT);

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(imageView,label);
                label.setAlignment(Pos.CENTER);
                mapGrid.add(stackPane,key.getX()-x+1,height-(key.getY()-y)+1);
            } else {
                Image image = new Image("/images/grass.png");
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(CELL_WIDTH);
                imageView.setFitHeight(CELL_HEIGHT);
                mapGrid.add(imageView,key.getX()-x+1,height-(key.getY()-y)+1);
            }

        } );
    }

    public void onSimulationStartClicked(){
       String[] moves = movesTextField.getText().split("\\s+");
        System.out.println("System wystartował");
        try {
            Stage newStage = new Stage();
            SimulationPresenter newPresenter = createNewSimulationWindow(newStage);

            List<MoveDirection> directions = OptionsParser.parse(moves);
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(6,3));
            ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
            List<Simulation> simulations = new ArrayList<>();
            GrassField grassField = new GrassField(10);
            newPresenter.setWorldMap(grassField);
            grassField.addObserver(newPresenter);
            simulations.add(new Simulation(positions,directions,grassField));
            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("System Zakończył działanie");
    }

    private SimulationPresenter createNewSimulationWindow(Stage newStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("simulationwindow.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter newPresenter = loader.getController();
        configureStage(viewRoot, newStage);
        newStage.show();
        return newPresenter;
    }

    private static void configureStage(BorderPane viewRoot, Stage newStage) {
        Image icon = new Image("/images/pig2.png");
        newStage.getIcons().add(icon);
        Scene scene = new Scene(viewRoot);
        newStage.setScene(scene);
        newStage.setTitle("Simulation Window");
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(()-> {
            drawMap(worldMap);
            System.out.println(worldMap.toString());
            infoLabel.setText(message);
        });

    }
}
