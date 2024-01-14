package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int count;
    private final List<Vector2d> positions;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int count) {
        this.count = count;
        this.positions = getPositions(maxWidth, maxHeight);
    }

    private static List<Vector2d> getPositions(int maxWidth, int maxHeight) {
        final List<Vector2d> positions = new ArrayList<>((maxWidth+1) * (maxHeight+1));
        for (int i = 0; i < maxWidth + 1; i++) {
            for (int j = 0; j < maxHeight + 1; j++) {
                positions.add(new Vector2d(i, j));
            }
        }
        return positions;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new Itr(count, positions);
    }

    private static class Itr implements Iterator<Vector2d> {
        private int count;
        private int rangeIndex;
        private final List<Vector2d> positions;

        public Itr(int count, List<Vector2d> positions) {
            this.count = count;
            this.positions = positions;
            rangeIndex = positions.size();
        }

        @Override
        public boolean hasNext() {
            return count > 0 && rangeIndex > 0;
        }

        @Override
        public Vector2d next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            count--;
            rangeIndex--;
            int i = (int) (Math.random() * rangeIndex);
            Collections.swap(positions, i, rangeIndex);
            return positions.get(rangeIndex);
        }
    }
}
