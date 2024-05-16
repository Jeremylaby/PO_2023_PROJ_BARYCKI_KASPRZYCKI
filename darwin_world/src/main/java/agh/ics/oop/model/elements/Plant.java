package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import javafx.scene.paint.Color;

import java.util.List;

public class Plant implements WorldElement {
    private final static Color STANDARD_PLANT_COLOR = Color.hsb(110, 0.6, 0.8);
    private final static Color EQUATOR_PLANT_COLOR = Color.hsb(110, 0.6, 0.5);
    private final static Color POISONED_PLANT_COLOR = Color.hsb(270, 0.5, 0.8);
    private final Vector2d position;
    private final boolean isPoisonous;
    private final boolean isEquator;

    public Plant(Vector2d position, boolean isPoisonous, boolean isEquator) {
        this.position = position;
        this.isPoisonous = isPoisonous;
        this.isEquator = isEquator;
    }

    public Plant(Vector2d position, boolean isPoisonous) {
        this(position, isPoisonous, false);
    }

    public Plant(Vector2d position) {
        this(position, false, false);
    }

    public boolean isPoisonous() {
        return isPoisonous;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public Color getColor() {
        if (isPoisonous) return POISONED_PLANT_COLOR;
        if (isEquator) return EQUATOR_PLANT_COLOR;
        return STANDARD_PLANT_COLOR;
    }
}
