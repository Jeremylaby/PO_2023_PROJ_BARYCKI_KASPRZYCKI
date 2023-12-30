package agh.ics.oop.model;

public class Plant implements WorldElement {
    private final Vector2d position;
    private final boolean isPoisonous;
    public Plant(Vector2d position, boolean isPoisonous) {
        this.position = position;
        this.isPoisonous = isPoisonous;
    }

    public boolean isPoisonous() {
        return isPoisonous;
    }

    public Plant(Vector2d position){
        this(position,false);
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
