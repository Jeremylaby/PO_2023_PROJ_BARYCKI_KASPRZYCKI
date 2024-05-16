package agh.ics.oop.presenter.start;

import agh.ics.oop.model.configuration.IllegalConfigurationValueException;
import agh.ics.oop.presenter.AlertDisplay;
import agh.ics.oop.presenter.simulation.SimulationPresenter;
import agh.ics.oop.simulation.DarwinSimulation;
import agh.ics.oop.simulation.Simulation;
import agh.ics.oop.simulation.SimulationEngine;
import agh.ics.oop.model.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class StartPresenter implements Initializable {
    @FXML
    private RadioButton plantsGrowthVariantPoison;
    @FXML
    private RadioButton plantsGrowthVariantEquator;
    @FXML
    private RadioButton genomeSequenceVariantBackAndForth;
    @FXML
    private RadioButton genomeSequenceVariantCycle;

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
    private TextField statsFileName;

    @FXML
    private ComboBox<String> savedConfigurations;
    @FXML
    private Button chooseDirectoryForStats;
    @FXML
    private Button deleteConfiguration;
    private String filePathToSaveStats = null;
    private SimulationEngine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = new SimulationEngine();
        attachTextFormatters();
        attachSavedConfigurationListener();
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

    private void attachSavedConfigurationListener() {
        savedConfigurations
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((options, oldValue, newValue) -> loadConfiguration(newValue));
    }

    private void attachFormatter(Spinner<Integer> spinner) {
        IntegerStringConverter converter = new IntegerStringConverter();
        TextFormatter<Integer> formatter = new TextFormatter<>(converter, spinner.getValue());
        spinner.getEditor().setTextFormatter(formatter);
    }

    @FXML
    private void onSimulationStartClicked() {
        try {
            Simulation simulation = new DarwinSimulation(getConfiguration(), filePathToSaveStats);
            resetDirectoryChoice();
            startSimulation(simulation);
        } catch (IllegalConfigurationValueException e) {
            AlertDisplay.showErrorAlert("Niepoprawna Wartość!", e.getMessage());
        } catch (IOException e) {
            AlertDisplay.showErrorAlert("Coś poszło nie tak!", e.getMessage());
        }
    }

    @FXML
    public void onConfigurationSaveClicked() {
        if (configurationName.getText().isBlank()) {
            AlertDisplay.showErrorAlert("Niepoprawna Wartość!", "Niepoprawna nazwa dla konfiguracji");
            return;
        }

        try {
            ConfigurationLoader.saveToFile(configurationName.getText(), getConfiguration());
            AlertDisplay.showSuccessAlert(
                    "Konfiguracja o nazwie \"%s\"\n Została pomyślnie zapisana."
                            .formatted(configurationName.getText())
            );
            updateComboBox();
        } catch (ConfigurationAlreadyExistsException | IllegalConfigurationValueException e) {
            AlertDisplay.showErrorAlert("Niepoprawna Wartość!", e.getMessage());
        } catch (IOException e) {
            AlertDisplay.showErrorAlert("Coś poszło nie tak!", e.getMessage());
        }
    }

    @FXML
    private void onChooseDirectoryClicked() {
        if (statsFileName.getText().isBlank()) {
            AlertDisplay.showErrorAlert("Niepoprawna Wartość", "Podaj nazwę pliku dla statystyk");
            return;
        }

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Wybierz folder do zapisu statystyk");
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            String directory = selectedDirectory.getAbsolutePath().replace("\\", "/");
            filePathToSaveStats = directory + "/" + statsFileName.getText();

            boolean alreadyExistsFile = Files.exists(Paths.get(filePathToSaveStats + ".csv"));
            if (alreadyExistsFile) {
                AlertDisplay.showErrorAlert("Niepoprawna Wartość", "Plik do zapisu statystyk o takiej nazwię już istnieje");
                resetDirectoryChoice();
                return;
            }

            chooseDirectoryForStats.setText("Wybrano folder: " + selectedDirectory.getName());
            chooseDirectoryForStats.getStyleClass().add("folder-chosen");
        }
    }

    @FXML
    private void deleteChoosedConfiguration() {
        if (savedConfigurations.getValue() == null) {
            AlertDisplay.showErrorAlert("Niepoprawna Wartość!", "Nie możesz usunąć tej pozycji");
            return;
        }
        try {
            ConfigurationLoader.removeFromFile(savedConfigurations.getValue());
            AlertDisplay.showSuccessAlert(
                    "Konfiguracja o nazwie \"%s\"\n Została pomyślnie usunięta."
                            .formatted(savedConfigurations.getValue())
            );
            updateComboBox();
        } catch (IOException e) {
            AlertDisplay.showErrorAlert("Coś poszło nie tak!", e.getMessage());
        }
        updateComboBox();
    }

    private void resetDirectoryChoice() {
        statsFileName.setText("");
        filePathToSaveStats = null;
        chooseDirectoryForStats.getStyleClass().remove("folder-chosen");
        chooseDirectoryForStats.setText("Wybierz folder");
    }

    private void loadConfiguration(String configName) {
        Configuration config = ConfigurationLoader.loadConfiguration(configName);

        genomeSequenceVariantBackAndForth.setSelected(config.genomeSequenceVariantBackAndForth());
        genomeSequenceVariantCycle.setSelected(!config.genomeSequenceVariantBackAndForth());

        plantsGrowthVariantPoison.setSelected(config.plantsGrowthVariantPoison());
        plantsGrowthVariantEquator.setSelected(!config.plantsGrowthVariantPoison());

        setSpinnerValue(mapWidth, config.mapWidth());
        setSpinnerValue(mapHeight, config.mapHeight());
        setSpinnerValue(plantsStartNum, config.plantsStartNum());
        setSpinnerValue(plantsEnergyValue, config.plantsEnergyValue());
        setSpinnerValue(plantsNumPerDay, config.plantsNumPerDay());
        setSpinnerValue(animalsStartNum, config.animalsStartNum());
        setSpinnerValue(animalsStartEnergy, config.animalsStartEnergy());
        setSpinnerValue(animalsEnergyToReproduce, config.animalsEnergyToReproduce());
        setSpinnerValue(animalsEnergyReproduceCost, config.animalsEnergyReproduceCost());
        setSpinnerValue(mutationsMinNum, config.mutationsMinNum());
        setSpinnerValue(mutationsMaxNum, config.mutationsMaxNum());
        setSpinnerValue(genomeSize, config.genomeSize());
    }

    private void setSpinnerValue(Spinner<Integer> spinner, int value) {
        spinner.getValueFactory().setValue(value);
    }

    private Configuration getConfiguration() throws IllegalConfigurationValueException {
        return new Configuration.Builder()
                .setMapWidth(mapWidth.getValue())
                .setMapHeight(mapHeight.getValue())
                .setPlantsStartNum(plantsStartNum.getValue())
                .setPlantsEnergyValue(plantsEnergyValue.getValue())
                .setPlantsNumPerDay(plantsNumPerDay.getValue())
                .setPlantsGrowthVariantPoison(plantsGrowthVariantPoison.isSelected())
                .setAnimalsStartNum(animalsStartNum.getValue())
                .setAnimalsStartEnergy(animalsStartEnergy.getValue())
                .setAnimalsEnergyToReproduce(animalsEnergyToReproduce.getValue())
                .setAnimalsEnergyReproduceCost(animalsEnergyReproduceCost.getValue())
                .setMutationsMinNum(mutationsMinNum.getValue())
                .setMutationsMaxNum(mutationsMaxNum.getValue())
                .setGenomeSize(genomeSize.getValue())
                .setGenomeSequenceVariantBackAndForth(genomeSequenceVariantBackAndForth.isSelected())
                .build();
    }

    private void startSimulation(Simulation simulation) throws IOException {
        SimulationPresenter presenter = WindowFactory.createSimulationWindow(simulation);
        presenter.setSimulation(simulation);
        simulation.addListener(presenter);
        engine.runSimulation(simulation);
    }

    private void updateComboBox() {
        savedConfigurations.getItems().clear();
        savedConfigurations.getItems().addAll(ConfigurationLoader.loadConfigurationsNames());
    }

    public void shutdown() {
        try {
            engine.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
