package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void move() {
        List<Integer> genes = List.of(1, 1, 1, 1);
        Genome genome = new Genome(false, 0, 0, genes);
        Animal animal = new Animal(new Vector2d(5, 5), genome, 100, 50);

        try {
            Field declaredField = Animal.class.getDeclaredField("orientation");
            declaredField.setAccessible(true);
            declaredField.set(animal, MapDirection.NORTH);
        } catch (Exception e) {
            fail();
        }

        animal.move(10, 10);
        animal.move(10, 10);
        animal.move(10, 10);
        animal.move(10, 10);
        animal.move(10, 10);
        animal.move(10, 10);
        animal.move(10, 10);

        assertEquals(new Vector2d(5, 4), animal.getPosition());
        assertEquals(93, animal.getEnergy());
        assertEquals(7, animal.getAge());
        assertEquals(MapDirection.NORTHWEST, animal.getOrientation());
    }

    @Test
    void dodge() {
        // TODO
    }

    @Test
    void eat() {
        Genome genome = new Genome(false, 0, 0, 5);
        Animal animal = new Animal(new Vector2d(5, 0), genome, 100, 50);

        animal.eat(20);

        assertEquals(120, animal.getEnergy());
    }

    @Test
    void updatePosition() {
        List<Integer> genes = List.of(0, 0, 0, 0, 0);
        Genome genome = new Genome(false, 0, 0, genes);
        Animal animal1 = new Animal(new Vector2d(5, 0), genome, 100, 50);
        Animal animal2 = new Animal(new Vector2d(10, 4), genome, 100, 50);

        try {
            Field declaredField = Animal.class.getDeclaredField("orientation");
            declaredField.setAccessible(true);
            declaredField.set(animal1, MapDirection.SOUTH);
            declaredField.set(animal2, MapDirection.EAST);
        } catch (Exception e) {
            fail();
        }

        animal1.move(10, 10);
        animal2.move(10, 10);

        assertEquals(1, animal1.getPosition().y());
        assertEquals(5, animal1.getPosition().x());
        assertEquals(4, animal2.getPosition().y());
        assertEquals(0, animal2.getPosition().x());
    }
}