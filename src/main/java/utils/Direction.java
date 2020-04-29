package utils;

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