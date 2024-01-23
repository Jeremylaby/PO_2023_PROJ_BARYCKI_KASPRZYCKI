package agh.ics.oop.model.configuration;

public class IllegalConfigurationValueException extends Exception {
    public IllegalConfigurationValueException(int value, String inputFieldName, String additionalInfo) {
        super("Wartość %d jest niepoprawna dla pola \"%s\"\n\n%s".formatted(value, inputFieldName, additionalInfo));
    }
}
