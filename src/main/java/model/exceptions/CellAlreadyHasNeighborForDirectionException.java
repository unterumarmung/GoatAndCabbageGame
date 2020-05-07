package model.exceptions;

import model.Cell;
import org.jetbrains.annotations.NotNull;
import utils.Direction;

public class CellAlreadyHasNeighborForDirectionException extends IllegalArgumentException {
    public final Cell cell;
    public final Direction direction;

    public CellAlreadyHasNeighborForDirectionException(@NotNull Cell cell, @NotNull Direction direction) {
        super("Cell already has a neighbor for direction = " + direction);
        this.cell = cell;
        this.direction = direction;
    }
}
