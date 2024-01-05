package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

public class RectangularMap extends AbstractWorldMap {
    private static final Vector2d LEFT_LOWER_CORNER = new Vector2d(0, 0);
    private final Vector2d rightuppercorner;
    private final int width;
    private final int height;

    public RectangularMap(int width, int height) {
        rightuppercorner = new Vector2d(width - 1, height - 1);
        this.width = width;
        this.height = height;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

//    public boolean canMoveTo(Vector2d position) {
//        return super.canMoveTo(position)
//                && position.follows(LEFT_LOWER_CORNER)
//                && position.precedes(rightuppercorner);
//    }

//    @Override
//    public Boundary getCurrentBounds() {
//        return new Boundary(LEFT_LOWER_CORNER,rightuppercorner);
//    }
}
