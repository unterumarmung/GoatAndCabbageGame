package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.Contract;
import xyz.unterumarmung.hash.Hashable;
import xyz.unterumarmung.model.Cell;

public interface GameObject extends Hashable {
    @Contract(pure = true)
    Cell cell();

    @Contract(pure = true)
    boolean isSolid();
}
