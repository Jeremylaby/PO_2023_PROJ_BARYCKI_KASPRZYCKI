package agh.ics.oop.model.map;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.configuration.Configuration;
import agh.ics.oop.model.configuration.IllegalConfigurationValueException;
import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.elements.AnimalsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Set;

class EquatorMapTest {
    private Configuration.Builder configBuilder;
    private AnimalsFactory animalsFactory;

    @BeforeEach
    void init() throws IllegalConfigurationValueException {
        configBuilder = new Configuration.Builder()
                .setMapWidth(10)
                .setMapHeight(10)
                .setAnimalsStartEnergy(100)
                .setAnimalsEnergyToReproduce(50)
                .setAnimalsEnergyReproduceCost(10)
                .setGenomeSize(10)
                .setGenomeSequenceVariantBackAndForth(false)
                .setMutationsMinNum(0)
                .setMutationsMaxNum(0)
                .setPlantsStartNum(5)
                .setPlantsNumPerDay(5);

        animalsFactory = new AnimalsFactory(configBuilder.build());
    }

    @Test
    void placeOneAnimal() {
        var equatorMap = new EquatorMap(configBuilder.build(), animalsFactory);
        var position = new Vector2d(3, 4);
        var animal = animalsFactory.createInitialAnimal(position);

        equatorMap.place(animal);

        Set<Animal> animalsAtPosition = equatorMap.getAnimals().get(position);
        assertEquals(1, animalsAtPosition.size());
        assertTrue(animalsAtPosition.contains(animal));
        assertTrue(5 <= equatorMap.getElements().toList().size());
        assertTrue(6 >= equatorMap.getElements().toList().size());
    }

    @Test
    void placeMultipleAnimals() {
        // then
        var equatorMap = new EquatorMap(configBuilder.build(), animalsFactory);
        var position1 = new Vector2d(3, 4);
        var position2 = new Vector2d(5, 5);
        var animal1 = animalsFactory.createInitialAnimal(position1);
        var animal2 = animalsFactory.createInitialAnimal(position1);
        var animal3 = animalsFactory.createInitialAnimal(position2);

        // when
        equatorMap.place(animal1);
        equatorMap.place(animal2);
        equatorMap.place(animal3);

        // then
        assertEquals(2, equatorMap.getAnimals().size());

        var animalsAtPosition1 = equatorMap.animalsAt(position1);
        var animalsAtPosition2 = equatorMap.animalsAt(position2);

        assertEquals(2, animalsAtPosition1.map(Set::size).orElse(0));
        assertEquals(1, animalsAtPosition2.map(Set::size).orElse(0));

        assertTrue(animalsAtPosition1.orElse(Collections.emptySet()).contains(animal1));
        assertTrue(animalsAtPosition1.orElse(Collections.emptySet()).contains(animal2));
        assertTrue(animalsAtPosition2.orElse(Collections.emptySet()).contains(animal3));

        assertTrue(7 >= equatorMap.getElements().toList().size());
        assertTrue(5 <= equatorMap.getElements().toList().size());
    }

    @Test
    void move() {
        var equatorMap = new EquatorMap(configBuilder.build(), animalsFactory);
        var position1 = new Vector2d(3, 4);
        var position2 = new Vector2d(1, 1);
        var animal1 = animalsFactory.createInitialAnimal(position1);
        var animal2 = animalsFactory.createInitialAnimal(position2);
        equatorMap.place(animal1);
        equatorMap.place(animal2);

        equatorMap.move(animal1);
        equatorMap.move(animal2);

        assertNotEquals(position1, animal1.getPosition());
        assertNotEquals(position2, animal2.getPosition());
        assertEquals(1, animal1.getAge());
        assertEquals(1, animal2.getAge());
    }

    @Test
    void feedAnimals() {
        // given
        var config = configBuilder
                .setMapWidth(5)
                .setMapHeight(5)
                .setAnimalsStartEnergy(100)
                .setPlantsEnergyValue(10)
                .setPlantsStartNum(25)
                .build();

        var equatorMap = new EquatorMap(config, animalsFactory);

        var position1 = new Vector2d(3, 4);
        var position2 = new Vector2d(1, 1);
        var animal1 = animalsFactory.createInitialAnimal(position1);
        var animal2 = animalsFactory.createInitialAnimal(position2);
        var animal3 = animalsFactory.createInitialAnimal(position2);
        equatorMap.place(animal1);
        equatorMap.place(animal2);
        equatorMap.place(animal3);
        animal3.eat(20);

        // when
        equatorMap.feedAnimals();

        // then
        assertEquals(110, animal1.getEnergy());
        assertEquals(100, animal2.getEnergy());
        assertEquals(130, animal3.getEnergy());
    }

    @Test
    void isAnimal() {
        var equatorMap = new EquatorMap(configBuilder.build(), animalsFactory);
        var position = new Vector2d(3, 4);
        var animal = animalsFactory.createInitialAnimal(position);
        equatorMap.place(animal);

        boolean isAnimalAtPosition1 = equatorMap.isAnimal(position);
        boolean isAnimalAtPosition2 = equatorMap.isAnimal(new Vector2d(3, 8));

        assertTrue(isAnimalAtPosition1);
        assertFalse(isAnimalAtPosition2);
    }

    @Test
    void animalsAt() {
        var equatorMap = new EquatorMap(configBuilder.build(), animalsFactory);
        var position = new Vector2d(3, 4);
        var animal1 = animalsFactory.createInitialAnimal(position);
        var animal2 = animalsFactory.createInitialAnimal(position);
        equatorMap.place(animal1);
        equatorMap.place(animal2);

        var animalsAtPosition1 = equatorMap.animalsAt(position);
        var animalsAtPosition2 = equatorMap.animalsAt(new Vector2d(3, 8));

        assertTrue(animalsAtPosition1.isPresent());
        assertTrue(animalsAtPosition1.orElse(Collections.emptySet()).contains(animal1));
        assertTrue(animalsAtPosition1.orElse(Collections.emptySet()).contains(animal2));
        assertTrue(animalsAtPosition2.isEmpty());
    }

    @Test
    void reproduceAnimals() {
    }

    @Test
    void getElements() {
        // given
        var config = configBuilder
                .setPlantsStartNum(10)
                .setPlantsNumPerDay(5)
                .build();

        var equatorMap = new EquatorMap(config, animalsFactory);

        var animal1 = animalsFactory.createInitialAnimal(new Vector2d(5, 2));
        var animal2 = animalsFactory.createInitialAnimal(new Vector2d(4, 7));
        var animal3 = animalsFactory.createInitialAnimal(new Vector2d(4, 7));
        equatorMap.place(animal1);
        equatorMap.place(animal2);
        equatorMap.place(animal3);

        equatorMap.growPlants();
        animal3.eat(100);

        // when
        var elements = equatorMap.getElements().toList();

        // then
        assertTrue(15 <= elements.size());
        assertTrue(17 >= elements.size());
        assertTrue(elements.contains(animal1));
        assertFalse(elements.contains(animal2));
        assertTrue(elements.contains(animal3));
    }

    @Test
    void growPlants() {
        var config = configBuilder
                .setPlantsStartNum(10)
                .setPlantsNumPerDay(5)
                .build();
        var equatorMap = new EquatorMap(config, animalsFactory);

        equatorMap.growPlants();
        equatorMap.growPlants();

        assertEquals(20, equatorMap.getPlants().size());
    }
}