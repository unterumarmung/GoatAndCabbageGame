package xyz.unterumarmung.model;

import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.events.MessageSource;
import xyz.unterumarmung.hash.Hashable;
import xyz.unterumarmung.model.events.CellMessage;
import xyz.unterumarmung.model.exceptions.CellAlreadyHasNeighborForDirectionException;
import xyz.unterumarmung.model.objects.GameObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static xyz.unterumarmung.utils.collections.ReadOnlyList.fromList;

public class Cell implements MessageSource, Hashable {
    private final Map<Direction, Cell> neighbors = new EnumMap<>(Direction.class);
    private final List<GameObject> objects = new ArrayList<>();
    private final MessageSender messageSender;

    public Cell(@NotNull MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Contract(pure = true)
    public Cell neighborCell(@NotNull Direction direction) {
        return neighbors.get(direction);
    }

    void setNeighbor(@NotNull Cell cell, @NotNull Direction direction) {
        if (neighbors.containsKey(direction) && neighbors.containsValue(cell))
            return;
        if (neighbors.containsKey(direction))
            throw new CellAlreadyHasNeighborForDirectionException(this, direction);

        neighbors.put(direction, cell);
        if (cell.neighborCell(direction.opposite()) == null) {
            cell.setNeighbor(this, direction.opposite());
        }
    }

    @Contract(pure = true)
    public Direction neighborDirection(@NotNull Cell cell) {
        for (var i : neighbors.entrySet()) {
            if (i.getValue().equals(cell))
                return i.getKey();
        }
        return null;
    }

    public void addObject(@NotNull GameObject object) {
        if (objects.contains(object))
            return;
        objects.add(object);
        messageSender.emitMessage(this, new CellMessage(CellMessage.Type.OBJECT_ENTERED, this, object));
    }

    public void removeObject(@NotNull GameObject object) {
        objects.remove(object);
        messageSender.emitMessage(this, new CellMessage(CellMessage.Type.OBJECT_LEAVED, this, object));
    }

    @Contract(pure = true)
    public @NotNull ReadOnlyList<GameObject> objects() {
        return fromList(new ArrayList<>(objects));
    }

    @Contract(pure = true)
    public @NotNull ReadOnlyList<CellWithDirection> neighbours() {
        return fromList(neighbors.entrySet().stream()
                .map(entry -> new CellWithDirection(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return hash();
    }
}
