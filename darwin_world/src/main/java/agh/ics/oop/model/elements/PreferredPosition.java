package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import javafx.scene.paint.Color;

public final class PreferredPosition implements WorldElement {
    private final static Color COLOR = Color.hsb(110, 0.2, 0.8);
    private final Vector2d position;

    public PreferredPosition(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public Color getColor() {
        return COLOR;
    }
}
