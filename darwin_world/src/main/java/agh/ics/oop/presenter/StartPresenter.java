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
import javafx.stage.DirectoryChooser;
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
    @FXML
    private ComboBox<String> savedConfigurations;
    @FXML
    private Button chooseDir;
    private static final String PATH_TO_CONFIG_FILE = "src/main/resources/configurations/configurations.json";
    private static final String PATH_TO_STATS_FILE = "src/main/resources/statistics/";
    private String directoryToSave = PATH_TO_STATS_FILE;
    private SimulationEngine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = new SimulationEngine();
        attachTextFormatters();
        updateComboBox();
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
        if (savedConfigurations.getValue() == null) {
            startSimulationWithConfig(getConfiguration());
        } else {
            startSimulationWithConfig(loadConfigurationsFromFile().get(savedConfigurations.getValue()));
        }
    }

    private void startSimulationWithConfig(Configuration configuration) {
        try {
            Simulation simulation = new Simulation(configuration, directoryToSave);
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

    private void updateComboBox() {
        savedConfigurations.getItems().clear();
        savedConfigurations.getItems().addAll(loadConfigurationsFromFile().keySet());
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

    private static Map<String, Configuration> loadConfigurationsFromFile() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(PATH_TO_CONFIG_FILE);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<Map<String, Configuration>>() {
                });
            } else {
                return new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public void onSimlationSaveToFile() {
        String fileName = PATH_TO_CONFIG_FILE;
        Map<String, Configuration> configurations = loadConfigurationsFromFile();
        String configName = configurationName.getText();
        if (configurations.containsKey(configName)) {
            showAlert(configName);
            System.out.println("Konfiguracja o takiej nazwie istnieje");
        } else {
            configurations.put(configName, getConfiguration());
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(new File(fileName), configurations);
                showSuccesAlert(configName);
                System.out.println("Konfiguracje zapisane do pliku JSON ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateComboBox();
        }

    }

    private void showSuccesAlert(String name) {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Informacja");
        informationAlert.setHeaderText("OK");
        informationAlert.setContentText("Konfiguracja o Nazwie: " + name + "\n Zosatala pomysnie zapisana do pliku JSON.");
        informationAlert.showAndWait();
    }

    private void showAlert(String name) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("INVALID NAME");
        alert.setContentText("Konfiguracja o takiej nazwie:" + name + " juz istnieje");
        alert.showAndWait();
    }

    public void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz folder do zapisu pliku");
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            directoryToSave = selectedDirectory.getAbsolutePath().replace("\\","/")+"/";
            System.out.println("Wybrany folder: " + directoryToSave);
        } else {
            System.out.println("Nie wybrano żadnego folderu.");
            directoryToSave = PATH_TO_STATS_FILE;
        }
    }

}
