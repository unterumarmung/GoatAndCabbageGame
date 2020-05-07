package model.exceptions;

import org.jetbrains.annotations.NotNull;

public class IllegalDimensionException extends IllegalArgumentException {
    public final int dimensionValue;
    public final @NotNull String dimensionName;

    public IllegalDimensionException(int dimensionValue, @NotNull String dimensionName) {
        super("Illegal value = " + dimensionValue + " of dimension " + dimensionName);
        this.dimensionValue = dimensionValue;
        this.dimensionName = dimensionName;
    }
}
