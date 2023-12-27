package agh.ics.oop.model;

import java.util.Objects;

public class Animal implements WorldElement{
    private MapDirection orientation;
    private Vector2d position;

    public Animal(Vector2d position) {
        this.orientation = MapDirection.NORTH;
        this.position=position;
    }

    public Animal() {this(new Vector2d(2,2));}

    public MapDirection getOrientation() {
        return orientation;
    }
    @Override
    public Vector2d getPosition() {
        return position;
    }



    @Override
    public String toString() {
        return orientation.toString();
    }
    public boolean isAt(Vector2d position){
        return Objects.equals(this.position,position);
    }
    void move(MoveDirection direction,MoveValidator validator){
        orientation= switch (direction) {
            case FORWARD,BACKWARD -> orientation;
            case RIGHT-> orientation.next();
            case LEFT -> orientation.previous();
        };
        Vector2d newposition = switch (direction){
            case FORWARD -> position.add(orientation.toUnitVector());
            case BACKWARD -> position.subtract(orientation.toUnitVector());
            case LEFT,RIGHT -> position;
        };
        if (validator.canMoveTo(newposition)){
            position=newposition;
        }
    }
}
