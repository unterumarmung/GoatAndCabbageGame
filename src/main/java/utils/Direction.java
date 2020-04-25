package utils;

public enum Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    static {
        NORTH._opposite = SOUTH;
        SOUTH._opposite = NORTH;
        WEST._opposite = EAST;
        EAST._opposite = WEST;
    }

    private Direction _opposite;

    public Direction opposite() {
        return _opposite;
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