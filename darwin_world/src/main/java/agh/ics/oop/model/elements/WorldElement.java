package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import javafx.scene.paint.Color;

public interface WorldElement {
    Vector2d getPosition();

    String getTexturePath();

    Color getColor();

}