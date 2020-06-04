package xyz.unterumarmung.utils.collections;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class ReadOnlyList<T> implements Iterable<T> {
    private final List<T> list;

    private ReadOnlyList(@NotNull List<T> list) {
        this.list = Collections.unmodifiableList(list);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull ReadOnlyList<T> fromList(List<T> list) {
        return new ReadOnlyList<>(list);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull ReadOnlyList<T> fromArray(T[] array) {
        return new ReadOnlyList<>(Arrays.asList(array));
    }

    @Contract(value = " -> new", pure = true)
    public static <T> @NotNull ReadOnlyList<T> empty() {
        return new ReadOnlyList<>(new ArrayList<>());
    }

    @Contract(value = "_ -> new", pure = true)
    @SafeVarargs
    public static <T> @NotNull ReadOnlyList<T> of(T... elements) {
        return new ReadOnlyList<>(Arrays.asList(elements));
    }

    @Contract(pure = true)
    public int size() {
        return list.size();
    }

    @Contract(pure = true)
    public boolean contains(T o) {
        return list.contains(o);
    }

    @Contract(pure = true)
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Contract(pure = true)
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Contract(pure = true)
    public List<T> toUnmodifiableList() {
        return list;
    }

    @Contract(pure = true)
    public T get(int index) {
        return list.get(index);
    }

    @Contract(pure = true)
    public int indexOf(T o) {
        return list.indexOf(o);
    }

    @Contract(pure = true)
    public int lastIndexOf(T o) {
        return list.lastIndexOf(o);
    }

    @Contract(pure = true)
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Contract(pure = true)
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Contract(pure = true)
    public ReadOnlyList<T> subList(int fromIndex, int toIndex) {
        return new ReadOnlyList<>(list.subList(fromIndex, toIndex));
    }

    @Contract(pure = true)
    public Stream<T> stream() {
        return list.stream();
    }

    @NotNull
    @Override
    @Contract(pure = true)
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
