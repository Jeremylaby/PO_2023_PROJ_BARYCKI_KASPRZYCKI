package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void toUnitVector() {
        var north = MapDirection.NORTH;
        var southeast = MapDirection.SOUTHEAST;

        var northVector = north.toUnitVector();
        var southeastVector = southeast.toUnitVector();

        assertEquals(new Vector2d(0, 1), northVector);
        assertEquals(new Vector2d(1, -1), southeastVector);
    }

    @Test
    void opposite() {
        var west = MapDirection.WEST;
        var northeast = MapDirection.NORTHEAST;

        var oppositeWest = west.opposite();
        var oppositeNortheast = northeast.opposite();

        assertEquals(MapDirection.EAST, oppositeWest);
        assertEquals(MapDirection.SOUTHWEST, oppositeNortheast);
    }

    @Test
    void rotate() {
        var south = MapDirection.SOUTH;

        var southRotatedBy3 = south.rotate(3);
        var southRotatedBy12 = south.rotate(12);

        assertEquals(MapDirection.NORTHWEST, southRotatedBy3);
        assertEquals(MapDirection.NORTH, southRotatedBy12);
        assertThrows(IllegalArgumentException.class, () -> south.rotate(-5));
    }
}