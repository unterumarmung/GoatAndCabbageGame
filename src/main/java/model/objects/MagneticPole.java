package model.objects;

public enum MagneticPole {
    NORTH,
    SOUTH;

    @Override
    public String toString() {
        return switch (this) {
            case SOUTH -> "MagneticPole.SOUTH";
            case NORTH -> "MagneticPole.NORTH";
        };
    }
}
