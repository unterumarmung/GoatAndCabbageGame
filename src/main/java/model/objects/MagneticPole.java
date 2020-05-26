package model.objects;

import utils.Direction;

public enum MagneticPole {
    NORTH,
    SOUTH;

    static {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
    }

    private MagneticPole opposite;

    public MagneticPole opposite() {
        return opposite;
    }

    @Override
    public String toString() {
        return switch (this) {
            case SOUTH -> "MagneticPole.SOUTH";
            case NORTH -> "MagneticPole.NORTH";
        };
    }
}
