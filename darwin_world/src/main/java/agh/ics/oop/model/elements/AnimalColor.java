package agh.ics.oop.model.elements;

import javafx.scene.paint.Color;

public class AnimalColor {
    private static final double HUE = 30;
    private static final double BRIGHTNESS = 0.7;
    private final Animal animal;
    private final int energyToReproduce;

    public AnimalColor(Animal animal, int energyToReproduce) {
        this.animal = animal;
        this.energyToReproduce = energyToReproduce;
    }

    public Color getColor() {
        return Color.hsb(HUE, getSaturation(), BRIGHTNESS);
    }

    private double getSaturation() {
        return Math.min(1, Math.max((double) animal.getEnergy() / (1.5 * energyToReproduce), 0) + 0.1);
    }
}
