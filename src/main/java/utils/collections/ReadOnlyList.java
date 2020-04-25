package utils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class ReadOnlyList<T> implements Iterable<T> {
    private final List<T> _list;

    private ReadOnlyList(@NotNull List<T> list) {
        _list = Collections.unmodifiableList(list);
    }

    public static <T> ReadOnlyList<T> fromList(List<T> list) {
        return new ReadOnlyList<>(list);
    }

    public static <T> ReadOnlyList<T> fromArray(T[] array) {
        return new ReadOnlyList<>(Arrays.asList(array));
    }

    public boolean contains(T o) {
        return _list.contains(o);
    }

    public boolean isEmpty() {
        return _list.isEmpty();
    }

    public boolean containsAll(Collection<?> c) {
        return _list.containsAll(c);
    }

    public List<T> toUnmodifiableList() {
        return _list;
    }

    public T get(int index) {
        return _list.get(index);
    }

    public int indexOf(T o) {
        return _list.indexOf(o);
    }

    public int lastIndexOf(T o) {
        return _list.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return _list.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return _list.listIterator(index);
    }

    public ReadOnlyList<T> subList(int fromIndex, int toIndex) {
        return new ReadOnlyList<>(_list.subList(fromIndex, toIndex));
    }

    public Stream<T> stream() {
        return _list.stream();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return _list.iterator();
    }
}
