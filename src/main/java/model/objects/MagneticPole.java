package model.objects;

import org.jetbrains.annotations.NotNull;

public enum MagneticPole {
    NORTH,
    SOUTH;

    static {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
    }

    private MagneticPole opposite;

    @NotNull
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
