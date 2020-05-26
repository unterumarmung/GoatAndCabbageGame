package model.objects;

import org.jetbrains.annotations.NotNull;
import utils.Direction;

public interface MagnitableObject extends HookableObject {
    boolean isMagnitableTo(@NotNull MagneticObject magneticObject, @NotNull Direction direction);
}
