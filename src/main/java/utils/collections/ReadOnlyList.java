package utils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class ReadOnlyList<T> implements Iterable<T> {
    private final List<T> list;

    private ReadOnlyList(@NotNull List<T> list) {
        this.list = Collections.unmodifiableList(list);
    }

    public static <T> ReadOnlyList<T> fromList(List<T> list) {
        return new ReadOnlyList<>(list);
    }

    public static <T> ReadOnlyList<T> fromArray(T[] array) {
        return new ReadOnlyList<>(Arrays.asList(array));
    }

    public boolean contains(T o) {
        return list.contains(o);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    public List<T> toUnmodifiableList() {
        return list;
    }

    public T get(int index) {
        return list.get(index);
    }

    public int indexOf(T o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(T o) {
        return list.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    public ReadOnlyList<T> subList(int fromIndex, int toIndex) {
        return new ReadOnlyList<>(list.subList(fromIndex, toIndex));
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public static <T> ReadOnlyList<T> empty() {
        return new ReadOnlyList<>(new ArrayList<>());
    }

    @SafeVarargs
    public static <T> ReadOnlyList<T> of(T... elements) {
        return new ReadOnlyList<>(Arrays.asList(elements));
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}
