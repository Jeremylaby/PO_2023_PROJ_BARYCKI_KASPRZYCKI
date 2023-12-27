package agh.ics.oop.model;

public class PositionAlreadyOccupiedException extends Exception {
    public PositionAlreadyOccupiedException(Vector2d vector2d) {
        super("Position " + vector2d.toString() +" is already occupied");
    }
}
