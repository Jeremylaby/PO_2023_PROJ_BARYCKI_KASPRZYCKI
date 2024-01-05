package agh.ics.oop.model.util;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomNumGenerator {
    private static final Random random = new Random();

    public static List<Integer> generateRandomIntList(int min, int max, int n) {
        return Stream.generate(() -> generateRandomInt(min, max)).limit(n).toList();
    }

    public static List<Integer> generateRandomUniqueIndexes(int numberOfIndexes, int maxRange) {
        List<Integer> indexes = new ArrayList<>(IntStream.rangeClosed(0, maxRange).boxed().toList());
        Collections.shuffle(indexes);
        return indexes.stream().limit(numberOfIndexes).toList();
    }

    public static int generateRandomIntWithoutK(int min, int max, int k) {
        List<Integer> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            if (i != k) {
                list.add(i);
            }
        }
        Collections.shuffle(list);
        return list.get(0);
    }

    public static int generateRandomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
