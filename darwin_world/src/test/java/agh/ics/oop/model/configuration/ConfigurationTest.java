package agh.ics.oop.model.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    void animalsEnergyToReproduce() {
        var configBuilder = new Configuration.Builder();

        assertThrows(IllegalConfigurationValueException.class, () -> {
            configBuilder
                    .setAnimalsEnergyReproduceCost(50)
                    .setAnimalsEnergyToReproduce(40);
        });
    }

    @Test
    void animalsEnergyReproduceCost() {
        var configBuilder = new Configuration.Builder();

        assertThrows(IllegalConfigurationValueException.class, () -> {
            configBuilder
                    .setAnimalsEnergyToReproduce(40)
                    .setAnimalsEnergyReproduceCost(50);
        });
    }

    @Test
    void mutationsMinNum() {
        var configBuilder = new Configuration.Builder();

        assertThrows(IllegalConfigurationValueException.class, () -> {
            configBuilder
                    .setMutationsMaxNum(10)
                    .setMutationsMinNum(15);
        });
    }

    @Test
    void mutationsMaxNum() {
        var configBuilder = new Configuration.Builder();

        assertThrows(IllegalConfigurationValueException.class, () -> {
            configBuilder
                    .setMutationsMinNum(15)
                    .setMutationsMaxNum(10);
        });
    }

    @Test
    void genomeSize() {
        var configBuilder = new Configuration.Builder();

        assertThrows(IllegalConfigurationValueException.class, () -> {
            configBuilder
                    .setMutationsMinNum(15)
                    .setGenomeSize(10);
        });
    }
}