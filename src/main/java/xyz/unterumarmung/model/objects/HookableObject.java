package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

public interface HookableObject extends SolidObject {
    @NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects();
}
