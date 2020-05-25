package model.objects;

import org.jetbrains.annotations.NotNull;
import utils.Direction;

public interface MovableObject extends GameObject {
    boolean move(@NotNull Direction direction);
    boolean canMoveTo(@NotNull Direction direction);
    boolean canReplace(@NotNull GameObject gameObject, @NotNull Direction direction);
}
