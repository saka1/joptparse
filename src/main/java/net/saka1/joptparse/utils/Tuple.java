package net.saka1.joptparse.utils;

import java.util.Objects;

public class Tuple<T, U> {
    public final T first;
    public final U second;

    private Tuple(T t, U u) {
        first = t;
        second = u;
    }

    public static <T, U> Tuple<T, U> create(T first, U second) {
        return new Tuple<>(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(first, tuple.first) &&
                Objects.equals(second, tuple.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
