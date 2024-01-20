package agh.ics.oop.presenter;

import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationEngine;
import agh.ics.oop.model.Configuration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StartPresenter implements Initializable {
    @FXML
    private RadioButton plantsGrowthVariantPoison;
    @FXML
    private RadioButton genomeSequenceVariantBackAndForth;
    @FXML
    private Spinner<Integer> mapWidth;
    @FXML
    private Spinner<Integer> mapHeight;
    @FXML
    private Spinner<Integer> plantsStartNum;
    @FXML
    private Spinner<Integer> plantsEnergyValue;
    @FXML
    private Spinner<Integer> plantsNumPerDay;
    @FXML
    private Spinner<Integer> animalsStartNum;
    @FXML
    private Spinner<Integer> animalsStartEnergy;
    @FXML
    private Spinner<Integer> animalsEnergyToReproduce;
    @FXML
    private Spinner<Integer> animalsEnergyReproduceCost;
    @FXML
    private Spinner<Integer> mutationsMinNum;
    @FXML
    private Spinner<Integer> mutationsMaxNum;
    @FXML
    private Spinner<Integer> genomeSize;
    @FXML
    private TextField configurationName;
    private static final String PATH_TO_CONFIG_FILE ="src/main/resources/configurations/configurations.json";
    private SimulationEngine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = new SimulationEngine();
        attachTextFormatters();
    }

    private void attachTextFormatters() {
        attachFormatter(mapWidth);
        attachFormatter(mapHeight);
        attachFormatter(plantsStartNum);
        attachFormatter(plantsEnergyValue);
        attachFormatter(plantsNumPerDay);
        attachFormatter(animalsStartNum);
        attachFormatter(animalsStartEnergy);
        attachFormatter(animalsEnergyToReproduce);
        attachFormatter(animalsEnergyReproduceCost);
        attachFormatter(mutationsMinNum);
        attachFormatter(mutationsMaxNum);
        attachFormatter(genomeSize);
    }

    private void attachFormatter(Spinner<Integer> spinner) {
        IntegerStringConverter converter = new IntegerStringConverter();
        TextFormatter<Integer> formatter = new TextFormatter<>(converter, spinner.getValue());
        spinner.getEditor().setTextFormatter(formatter);
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
                mapWidth.getValue(),
                mapHeight.getValue(),
                plantsStartNum.getValue(),
                plantsEnergyValue.getValue(),
                plantsNumPerDay.getValue(),
                plantsGrowthVariantPoison.isSelected(),
                animalsStartNum.getValue(),
                animalsStartEnergy.getValue(),
                animalsEnergyToReproduce.getValue(),
                animalsEnergyReproduceCost.getValue(),
                mutationsMinNum.getValue(),
                mutationsMaxNum.getValue(),
                genomeSize.getValue(),
                genomeSequenceVariantBackAndForth.isSelected()
        );
    }

    private void startSimulation(Simulation simulation) throws Exception {
        SimulationPresenter presenter = createSimulationWindow(simulation);
        presenter.setSimulation(simulation);
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
    private static Map<String, Configuration> loadConfigurationsFromFile(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(fileName);
            if (file.exists()) {
                return objectMapper.readValue(file, new HashMap<String, Configuration>().getClass());
            } else {
                return new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    public void onSimlationSaveToFile() {
        String fileName=PATH_TO_CONFIG_FILE;
        Map<String, Configuration> configurations = loadConfigurationsFromFile(fileName);
        String configName = configurationName.getText();
        if(configurations.containsKey(configName)){
            showAlert("INVALID NAME","Konfiguracja o takiej nazwie już istnieje");
        }else{
            configurations.put(configName,getConfiguration());
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(new File(fileName), configurations);

                System.out.println("Konfiguracje zapisane do pliku JSON ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
