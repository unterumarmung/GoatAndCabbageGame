package model;

import events.MessageSender;
import model.exceptions.PointIsNotInFieldRangeException;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Point;
import utils.collections.ReadOnlyList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameField {
    private final @NotNull Map<Point, Cell> _cells = new HashMap<>();
    private final int _width;
    private final int _height;
    private final @NotNull Point _exitPoint;
    private final @NotNull MessageSender _messageSender;
    private final @NotNull Cabbage _cabbage;

    public GameField(int width, int height, @NotNull Point exitPoint, @NotNull MessageSender messageSender) {
        _width = width;
        _height = height;
        assertPointInRange(exitPoint);
        _exitPoint = exitPoint;
        _messageSender = messageSender;
        setup();
        _cabbage = new Cabbage(cell(exitPoint));
    }

    private void setup() {
        for (int y = 0; y < _height; ++y) {
            for (int x = 0; x < _width; ++x) {
                var point = new Point(x, y);
                var cell = new Cell(_messageSender);
                if (x > 0)
                    cell(new Point(point.x - 1, point.y)).setNeighbor(cell, Direction.EAST);
                if (y > 0)
                    cell(new Point(point.x, point.y - 1)).setNeighbor(cell, Direction.SOUTH);
                _cells.put(point, cell);
            }
        }
    }

    public Cell cell(@NotNull Point point) {
        assertPointInRange(point);
        return _cells.get(point);
    }

    private void assertPointInRange(@NotNull Point point) {
        if (point.x < 0
                || point.x >= width()
                || point.y < 0
                || point.y > height()) {
            throw new PointIsNotInFieldRangeException(point, width(), height());
        }
    }

    public int width() {
        return _width;
    }

    public int height() {
        return _height;
    }

    public Goat goat() {
        for (var cell : _cells.entrySet()) {
            var possibleGoat = cell.getValue().objects().stream().filter(gameObject -> gameObject instanceof Goat).findFirst();
            if (possibleGoat.isPresent())
                return (Goat) possibleGoat.get();
        }
        return null;
    }

    public ReadOnlyList<CellWithPosition> cells() {
        return ReadOnlyList.fromList(_cells.entrySet().stream().map(entry -> new CellWithPosition(entry.getValue(), entry.getKey())).collect(Collectors.toList()));
    }
}
