package agh.ics.oop.model.elements;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.IllegalConfigurationValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class AnimalFactoryTest {
    private Configuration config;
    private AnimalsFactory animalsFactory;

    @BeforeEach
    void init() throws IllegalConfigurationValueException {
        config = new Configuration.Builder()
                .setAnimalsStartEnergy(100)
                .setAnimalsEnergyToReproduce(50)
                .setAnimalsEnergyReproduceCost(10)
                .setGenomeSize(10)
                .setGenomeSequenceVariantBackAndForth(false)
                .setMutationsMinNum(0)
                .setMutationsMaxNum(0)
                .build();

        animalsFactory = new AnimalsFactory(config);
    }

    @Test
    void createInitialAnimal() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal = animalsFactory.createInitialAnimal(position);

        assertEquals(position, animal.getPosition());
        assertEquals(config.animalsStartEnergy(), animal.getEnergy());
        assertEquals(0, animal.getAge());
        assertNull(animal.getFather());
        assertNull(animal.getFather());
        assertEquals(config.genomeSize(), animal.getGenes().size());
        assertEquals(0, animal.getKidsNumber());
    }

    @Test
    void makeChildProperGenes() {
        Animal father = animalsFactory.createInitialAnimal(new Vector2d(2, 2));
        Animal mother = animalsFactory.createInitialAnimal(new Vector2d(2, 2));
        father.updateEnergy(-30);
        mother.updateEnergy(-70);

        Animal child = animalsFactory.makeChild(father, mother);

        assertEquals(2 * config.animalsEnergyReproduceCost(), child.getEnergy());
        assertEquals(new Vector2d(2, 2), child.getPosition());
        assertTrue(
                Objects.equals(father.getLeftGenesSlice(7), child.getGenes().subList(0, 7)) ||
                        Objects.equals(mother.getLeftGenesSlice(7), child.getGenes().subList(0, 7)) ||
                        Objects.equals(father.getRightGenesSlice(7), child.getGenes().subList(3, 10)) ||
                        Objects.equals(mother.getRightGenesSlice(7), child.getGenes().subList(3, 10))
        );
        assertTrue(
                Objects.equals(father.getLeftGenesSlice(3), child.getGenes().subList(0, 3)) ||
                        Objects.equals(mother.getLeftGenesSlice(3), child.getGenes().subList(0, 3)) ||
                        Objects.equals(father.getRightGenesSlice(3), child.getGenes().subList(7, 10)) ||
                        Objects.equals(mother.getRightGenesSlice(3), child.getGenes().subList(7, 10))
        );
    }

    @Test
    void updateFamilyTree() throws NoSuchFieldException, IllegalAccessException {
        // given
        Field descendantsNumberField = Animal.class.getDeclaredField("descendantsNumber");
        descendantsNumberField.setAccessible(true);
        Animal animal1 = animalsFactory.createInitialAnimal(new Vector2d(4, 7));
        Animal animal2 = animalsFactory.createInitialAnimal(new Vector2d(4, 7));

        // when
        Animal animal3 = animalsFactory.makeChild(animal1, animal2);
        Animal animal4 = animalsFactory.makeChild(animal1, animal2);
        Animal animal5 = animalsFactory.makeChild(animal3, animal4);
        Animal animal6 = animalsFactory.makeChild(animal3, animal4);

        // then
        assertEquals(2, animal1.getKidsNumber());
        assertEquals(2, animal2.getKidsNumber());
        assertEquals(4, descendantsNumberField.get(animal1));
        assertEquals(4, descendantsNumberField.get(animal2));
        assertEquals(2, animal3.getKidsNumber());
        assertEquals(2, animal4.getKidsNumber());
        assertEquals(2, descendantsNumberField.get(animal3));
        assertEquals(2, descendantsNumberField.get(animal4));
        assertEquals(0, descendantsNumberField.get(animal5));
        assertEquals(0, descendantsNumberField.get(animal6));
    }
}