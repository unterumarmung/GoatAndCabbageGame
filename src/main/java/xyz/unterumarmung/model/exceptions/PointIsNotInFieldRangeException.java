package xyz.unterumarmung.model.exceptions;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.utils.Point;

public class PointIsNotInFieldRangeException extends IllegalArgumentException {
    public final Point point;
    public final int fieldWidth;
    public final int fieldHeight;

    public PointIsNotInFieldRangeException(@NotNull Point point, int fieldWidth, int fieldHeight) {
        super("Point = " + point + " is not in range when fieldWidth = " + fieldWidth + " and fieldHeight = " + fieldHeight);
        this.point = point;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
    }
}
