package xyz.unterumarmung.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import static xyz.unterumarmung.utils.collections.ReadOnlyList.of;

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

    @Contract(pure = true)
    public static @NotNull ReadOnlyList<Direction> all() {
        return of(NORTH, EAST, SOUTH, WEST);
    }

    @NotNull
    public Direction opposite() {
        return opposite;
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