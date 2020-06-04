package xyz.unterumarmung.model.exceptions;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.utils.Direction;

public class CellAlreadyHasNeighborForDirectionException extends IllegalArgumentException {
    public final Cell cell;
    public final Direction direction;

    public CellAlreadyHasNeighborForDirectionException(@NotNull Cell cell, @NotNull Direction direction) {
        super("Cell already has a neighbor for direction = " + direction);
        this.cell = cell;
        this.direction = direction;
    }
}
