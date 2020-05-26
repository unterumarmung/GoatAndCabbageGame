package model.objects;

import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Pair;
import utils.collections.ReadOnlyList;

public interface HookableObject extends SolidObject {
    @NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects();
}
