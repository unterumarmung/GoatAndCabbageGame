package xyz.unterumarmung.model.objects;

import xyz.unterumarmung.hash.Hashable;
import xyz.unterumarmung.model.Cell;
import org.jetbrains.annotations.Contract;

public interface GameObject extends Hashable {
    @Contract(pure = true)
    Cell cell();

    @Contract(pure = true)
    boolean isSolid();
}
