package agh.ics.oop.presenter;

public class ConfigurationAlreadyExistsException extends Exception {
    public ConfigurationAlreadyExistsException(String configurationName) {
        super("Konfiguracja o nazwie \"" + configurationName + "\" już istnieje");
    }
}
