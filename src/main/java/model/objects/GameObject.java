package model.objects;

import hash.Hashable;
import model.Cell;
import org.jetbrains.annotations.Contract;

public interface GameObject extends Hashable {
    @Contract(pure = true)
    Cell cell();

    @Contract(pure = true)
    boolean isSolid();
}
