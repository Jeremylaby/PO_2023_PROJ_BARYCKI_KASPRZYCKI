package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;

public class Plant implements WorldElement {
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
}
