package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.utils.Direction;

public interface MagnitableObject extends HookableObject {
    boolean isMagnitableTo(@NotNull MagneticObject magneticObject, @NotNull Direction direction);
}
