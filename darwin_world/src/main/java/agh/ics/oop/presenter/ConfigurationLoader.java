package agh.ics.oop.presenter;

import agh.ics.oop.model.Configuration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigurationLoader {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PATH_TO_CONFIG_FILE = "src/main/resources/configurations/configurations.json";

    public static void saveToFile(String configName, Configuration config) throws ConfigurationAlreadyExistsException, IOException {
        Map<String, Configuration> configurations = loadConfigurationsFromFile();

        if (configurations.containsKey(configName)) {
            throw new ConfigurationAlreadyExistsException(configName);
        } else {
            configurations.put(configName, config);
            objectMapper.writeValue(new File(PATH_TO_CONFIG_FILE), configurations);
        }
    }

    public static Configuration loadConfiguration(String configName) {
        return loadConfigurationsFromFile().get(configName);
    }

    public static Set<String> loadConfigurationsNames() {
        return loadConfigurationsFromFile().keySet();
    }

    public static Map<String, Configuration> loadConfigurationsFromFile() {
        try {
            File file = new File(PATH_TO_CONFIG_FILE);
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
