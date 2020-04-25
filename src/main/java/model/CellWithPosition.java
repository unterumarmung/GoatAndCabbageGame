package model;

import org.jetbrains.annotations.NotNull;
import utils.Point;

public class CellWithPosition {
    public final @NotNull Cell cell;
    public final @NotNull Point position;

    public CellWithPosition(@NotNull Cell cell, @NotNull Point position) {
        this.cell = cell;
        this.position = position;
    }
}
