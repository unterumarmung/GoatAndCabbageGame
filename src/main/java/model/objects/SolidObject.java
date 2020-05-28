package model.objects;

import org.jetbrains.annotations.Contract;

public interface SolidObject extends GameObject {
    @Contract(pure = true)
    @Override
    default boolean isSolid() {
        return true;
    }
}
