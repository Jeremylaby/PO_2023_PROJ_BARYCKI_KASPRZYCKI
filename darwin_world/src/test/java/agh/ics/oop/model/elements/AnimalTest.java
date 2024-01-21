package agh.ics.oop.model.elements;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

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

//    @Test
//    void makeChild() {
//        Genome genome1 = new Genome(false, 0, 0, 10);
//        Genome genome2 = new Genome(false, 0, 0, 10);
//        Animal animal1 = new Animal(new Vector2d(2, 2), genome1, 70, 50);
//        Animal animal2 = new Animal(new Vector2d(2, 2), genome2, 30, 50);
//
//        Animal animal3 = animal1.makeChild(animal2, 20, 50);
//
//        assertEquals(40, animal3.getEnergy());
//        assertEquals(new Vector2d(2, 2), animal3.getPosition());
//        assertTrue(
//                Objects.equals(genome1.getLeftGenesSlice(7), animal3.getGenes().subList(0, 7)) ||
//                        Objects.equals(genome2.getLeftGenesSlice(7), animal3.getGenes().subList(0, 7)) ||
//                        Objects.equals(genome1.getRightGenesSlice(7), animal3.getGenes().subList(3, 10)) ||
//                        Objects.equals(genome2.getRightGenesSlice(7), animal3.getGenes().subList(3, 10))
//        );
//        assertTrue(
//                Objects.equals(genome1.getLeftGenesSlice(3), animal3.getGenes().subList(0, 3)) ||
//                        Objects.equals(genome2.getLeftGenesSlice(3), animal3.getGenes().subList(0, 3)) ||
//                        Objects.equals(genome1.getRightGenesSlice(3), animal3.getGenes().subList(7, 10)) ||
//                        Objects.equals(genome2.getRightGenesSlice(3), animal3.getGenes().subList(7, 10))
//        );
//    }
//
//    @Test
//    void updateFamilyTree() {
//        Genome genome1 = new Genome(false, 0, 0, 10);
//        Genome genome2 = new Genome(false, 0, 0, 10);
//        Animal animal1 = new Animal(new Vector2d(2, 2), genome1, 100, 50);
//        Animal animal2 = new Animal(new Vector2d(2, 2), genome2, 100, 50);
//
//        Animal animal3 = animal1.makeChild(animal2, 10, 50);
//        Animal animal4 = animal1.makeChild(animal2, 10, 50);
//
//        Animal animal5 = animal3.makeChild(animal4, 10, 50);
//        Animal animal6 = animal3.makeChild(animal4, 10, 50);
//
//
//        assertEquals(2, animal1.getKidsNumber());
//        assertEquals(2, animal2.getKidsNumber());
//        assertEquals(4, animal1.getsDescendantNumber());
//        assertEquals(4, animal2.getsDescendantNumber());
//        assertEquals(2, animal3.getKidsNumber());
//        assertEquals(2, animal4.getKidsNumber());
//        assertEquals(2, animal3.getsDescendantNumber());
//        assertEquals(2, animal4.getsDescendantNumber());
//        assertEquals(0, animal5.getsDescendantNumber());
//        assertEquals(0, animal6.getsDescendantNumber());
//    }

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