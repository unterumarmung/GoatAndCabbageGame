package model;

import events.MessageSender;
import events.MessageSource;
import model.events.CellMessage;
import model.exceptions.CellAlreadyHasNeighborForDirectionException;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.collections.ReadOnlyList;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Cell implements MessageSource {
    private final Map<Direction, Cell> _neighbors = new EnumMap<>(Direction.class);
    private final List<GameObject> _objects = new ArrayList<>();
    private final MessageSender _messageSender;

    public Cell(@NotNull MessageSender messageSender) {
        _messageSender = messageSender;
    }

    public Cell neighborCell(@NotNull Direction direction) {
        return _neighbors.get(direction);
    }

    void setNeighbor(@NotNull Cell cell, @NotNull Direction direction) {
        if (_neighbors.containsKey(direction) && _neighbors.containsValue(cell))
            return;
        if (_neighbors.containsKey(direction))
            throw new CellAlreadyHasNeighborForDirectionException(this, direction);

        _neighbors.put(direction, cell);
        if (cell.neighborCell(direction.opposite()) == null) {
            cell.setNeighbor(this, direction.opposite());
        }
    }

    public Direction isNeighbor(@NotNull Cell cell) {
        for (var i : _neighbors.entrySet()) {
            if (i.getValue().equals(cell))
                return i.getKey();
        }
        return null;
    }

    public void addObject(@NotNull GameObject object) {
        _objects.add(object);
        _messageSender.emitMessage(this, new CellMessage(CellMessage.Type.OBJECT_ENTERED, this, object));
    }

    public void removeObject(@NotNull GameObject object) {
        _objects.remove(object);
        _messageSender.emitMessage(this, new CellMessage(CellMessage.Type.OBJECT_LEAVED, this, object));
    }

    public @NotNull ReadOnlyList<GameObject> objects() {
        return ReadOnlyList.fromList(_objects);
    }
}
