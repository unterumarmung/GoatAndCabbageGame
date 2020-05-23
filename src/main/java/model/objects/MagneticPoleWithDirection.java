package model.objects;

import org.jetbrains.annotations.NotNull;
import utils.Direction;

public class MagneticPoleWithDirection {
    public final @NotNull MagneticPole magneticPole;
    public final @NotNull Direction direction;

    public MagneticPoleWithDirection(@NotNull MagneticPole magneticPole, @NotNull Direction direction) {
        this.magneticPole = magneticPole;
        this.direction = direction;
    }
}
