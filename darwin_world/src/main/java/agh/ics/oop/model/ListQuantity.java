package agh.ics.oop.model;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public record ListQuantity(List<Integer> list,Integer quantity) {
    @Override
    public String toString() {
        return new String((getListString() +
                ", wystąpienia:" + quantity +
                '\n').getBytes(), StandardCharsets.UTF_8);
    }

    public String getListString() {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("", "[", "]"));
    }
}
