package model;

import events.MessageSender;
import model.exceptions.IllegalDimensionException;
import model.exceptions.PointIsNotInFieldRangeException;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Point;
import utils.collections.ReadOnlyList;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameField {
    private final @NotNull Map<Point, Cell> cells = new HashMap<>();
    private final int width;
    private final int height;
    private final @NotNull Point exitPoint;
    private final @NotNull MessageSender messageSender;
    private final @NotNull Cabbage cabbage;
    private Goat goat;

    public GameField(int width, int height, @NotNull Point exitPoint, @NotNull MessageSender messageSender) {
        assertDimensionIsCorrect(width, "width");
        assertDimensionIsCorrect(height, "height");
        this.width = width;
        this.height = height;
        assertPointInRange(exitPoint);
        this.exitPoint = exitPoint;
        this.messageSender = messageSender;
        setup();
        cabbage = new Cabbage(cell(exitPoint));
    }

    private void setup() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                var point = new Point(x, y);
                var cell = new Cell(messageSender);
                if (x > 0)
                    cell(new Point(point.x - 1, point.y)).setNeighbor(cell, Direction.EAST);
                if (y > 0)
                    cell(new Point(point.x, point.y - 1)).setNeighbor(cell, Direction.SOUTH);
                cells.put(point, cell);
            }
        }
    }

    public Cell cell(@NotNull Point point) {
        if (!isPointInRange(point)) {
            return null;
        }
        return cells.get(point);
    }

    private boolean isPointInRange(@NotNull Point point) {
        return point.x >= 0
                && point.x < width()
                && point.y >= 0
                && point.y < height();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Goat goat() {
        if (goat != null)
            return goat;
        for (var cell : cells.entrySet()) {
            var possibleGoat = cell.getValue().objects().stream().filter(gameObject -> gameObject instanceof Goat).findFirst();
            possibleGoat.ifPresent(object -> goat = (Goat) object);
        }
        return goat;
    }

    public ReadOnlyList<CellWithPosition> cells() {
        return ReadOnlyList.fromList(cells.entrySet().stream().map(entry -> new CellWithPosition(entry.getValue(), entry.getKey())).collect(Collectors.toList()));
    }

    public Point exitPoint() {
        return exitPoint;
    }

    private void assertPointInRange(@NotNull Point point) {
        if (!isPointInRange(point))
            throw new PointIsNotInFieldRangeException(point, width, height);
    }

    private void assertDimensionIsCorrect(int dimension, @NotNull String name) {
        if (dimension <= 0)
            throw new IllegalDimensionException(dimension, name);
    }
}
