package utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import utils.collections.ReadOnlyList;

import static utils.collections.ReadOnlyList.*;

public enum Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    static {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
        WEST.opposite = EAST;
        EAST.opposite = WEST;
    }

    private Direction opposite;

    @NotNull
    public Direction opposite() {
        return opposite;
    }

    @Contract(pure = true)
    public static @NotNull ReadOnlyList<Direction> all() {
        return of(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Direction.NORTH";
            case SOUTH -> "Direction.SOUTH";
            case WEST -> "Direction.WEST";
            case EAST -> "Direction.EAST";
        };
    }
}