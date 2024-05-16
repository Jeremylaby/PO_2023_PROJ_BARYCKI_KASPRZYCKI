package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import javafx.scene.paint.Color;

import java.util.List;

public interface WorldElement {
    Vector2d getPosition();

    Color getColor();

    default boolean hasDominatingGenes(List<Integer> dominatingGenes) {
        return false;
    }

    default boolean isSelectable() {
        return false;
    }
}
