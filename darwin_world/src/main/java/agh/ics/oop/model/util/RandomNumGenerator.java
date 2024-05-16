package agh.ics.oop.model.util;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomNumGenerator {
    private static final Random RANDOM = new Random();

    public static List<Integer> randomIntList(int min, int max, int n) {
        return Stream.generate(() -> randomInt(min, max)).limit(n).toList();
    }

    public static List<Integer> randomUniqueIndexes(int numberOfIndexes, int maxRange) {
        List<Integer> indexes = new ArrayList<>(IntStream.rangeClosed(0, maxRange).boxed().toList());
        Collections.shuffle(indexes);
        return indexes.stream().limit(numberOfIndexes).toList();
    }

    public static int randomIntWithoutK(int min, int max, int k) {
        List<Integer> list = new ArrayList<>(max-min);
        for (int i = min; i <= max; i++) {
            if (i != k) {
                list.add(i);
            }
        }
        Collections.shuffle(list);
        return list.get(0);
    }

    public static int randomInt(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }
}
