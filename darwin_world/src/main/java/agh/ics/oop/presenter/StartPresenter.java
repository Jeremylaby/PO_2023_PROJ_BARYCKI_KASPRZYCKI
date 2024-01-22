package agh.ics.oop.presenter;

import agh.ics.oop.simulation.DarwinSimulation;
import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationEngine;
import agh.ics.oop.model.Configuration;
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
    private Button chooseDirectoryForStats;
    private String directoryToSave = null;
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
            startSimulationWithConfig(ConfigurationLoader.loadConfiguration(savedConfigurations.getValue()));
        }
    }

    private void startSimulationWithConfig(Configuration configuration) {
        try {
            Simulation simulation = new DarwinSimulation(configuration, directoryToSave);
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
        savedConfigurations.getItems().addAll(ConfigurationLoader.loadConfigurationsNames());
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

    public void onConfigurationSaveToFile() {
        try {
            ConfigurationLoader.saveToFile(configurationName.getText(), getConfiguration());
            showSuccessAlert(configurationName.getText());
            updateComboBox();
        } catch (ConfigurationAlreadyExistsException e) {
            showErrorAlert(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showSuccessAlert(String name) {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Informacja");
        informationAlert.setHeaderText("OK");
        informationAlert.setContentText("Konfiguracja o nazwie \"" + name + "\"\n Została pomyślnie zapisana do pliku JSON.");
        informationAlert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("INVALID NAME");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz folder do zapisu statystyk");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            directoryToSave = selectedDirectory.getAbsolutePath().replace("\\","/")+"/";
            chooseDirectoryForStats.setText("Wybrano folder: " + selectedDirectory.getName());
            chooseDirectoryForStats.getStyleClass().add("folder-chosen");
        }
    }

}
