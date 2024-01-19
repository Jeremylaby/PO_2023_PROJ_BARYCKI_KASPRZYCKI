package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import javafx.scene.paint.Color;

public class Plant implements WorldElement {
    private final static Color STANDARD_PLANT_COLOR = Color.hsb(110, 0.6, 0.8);
    private final static Color POISONED_PLANT_COLOR = Color.hsb(270, 0.5, 0.8);
    private final Vector2d position;
    private final boolean isPoisonous;

    public Plant(Vector2d position, boolean isPoisonous) {
        this.position = position;
        this.isPoisonous = isPoisonous;
    }

    public Plant(Vector2d position){
        this(position,false);
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
    public String getTexturePath() {
        return "images/grass.png";
    }

    @Override
    public Color getColor() {
        return isPoisonous ? POISONED_PLANT_COLOR : STANDARD_PLANT_COLOR;
    }
}
