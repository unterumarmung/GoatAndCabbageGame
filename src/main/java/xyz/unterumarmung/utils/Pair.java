package xyz.unterumarmung.utils;

import java.util.Objects;

public final class Pair<TFirst, TSecond> {
    public final TFirst first;
    public final TSecond second;

    public Pair(TFirst first, TSecond second) {
        this.first = first;
        this.second = second;
    }

    @SuppressWarnings("unchecked")
    public <T> Pair<T, TSecond> castFirst() {
        return new Pair<>((T) first, second);
    }

    @SuppressWarnings("unchecked")
    public <T> Pair<TFirst, T> castSecond() {
        return new Pair<>(first, (T) second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
