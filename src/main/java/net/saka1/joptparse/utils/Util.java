package net.saka1.joptparse.utils;

import java.util.*;
import java.util.stream.Collectors;

public final class Util {
    private Util() {
    }

    public static <T> T head(List<T> list) {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return list.get(0);
    }

    public static <T> List<T> tail(List<T> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.subList(1, list.size());
    }

    public static List<String> splitCodePointList(String string) {
        return string.codePoints()
                .mapToObj(Character::toChars)
                .map(String::new)
                .collect(Collectors.toList());
    }

    public static String concat(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append(s);
        }
        return builder.toString();
    }

    /**
     * create shallow copy of the list.
     */
    public static <E> List<E> copyList(List<E> list) {
        List<E> result = new ArrayList<>();
        result.addAll(list);
        return result;
    }
}
