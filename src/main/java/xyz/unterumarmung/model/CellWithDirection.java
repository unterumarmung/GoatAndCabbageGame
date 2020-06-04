package xyz.unterumarmung.model;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.utils.Direction;

public class CellWithDirection {
    public final @NotNull Cell cell;
    public final @NotNull Direction direction;

    public CellWithDirection(@NotNull Cell cell, @NotNull Direction direction) {
        this.cell = cell;
        this.direction = direction;
    }
}
