package agh.ics.oop.model.elements;

import static org.junit.jupiter.api.Assertions.*;

class AnimalFactoryTest {
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
}